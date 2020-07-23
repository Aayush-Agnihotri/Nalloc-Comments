/*
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.github.nalloc.impl;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

import com.github.nalloc.NativeHeapAllocator;
import com.github.nalloc.Pointer;

/**
 * Unit tests for {@link HeapPointer}.
 *
 * @author Antti Laisi
 */
public class HeapPointerTest {

	final NativeHeapAllocator allocator = NativeHeapAllocator.Factory.create(Val.class);

	/* Aayush
	This method allocates two structs and creates their pointers, s1 and s2, then switches their memory addresses, and checks if the fields previously set stay the same.
	*/
	@Test
	public void shouldSetPointerAddress() {
		try(Pointer<Val> s1 = allocator.malloc(Val.class);
				Pointer<Val> s2 = allocator.malloc(Val.class)) {
			s1.deref().val(1);
			s2.deref().val(2);

			long tmp = s1.address();
			s1.address(s2.address());
			s2.address(tmp);

			assertEquals(1, s2.deref().val());
			assertEquals(2, s1.deref().val());
		}
	}

	/* Aayush
	This method allocates the val struct with pointer ptr, sets the val int field to 99, clones ptr and names it clone, and finally checks if the addresses and field values of the original and clone are equal.
	*/
	@Test
	public void shouldClonePointer() {
		try(Pointer<Val> ptr = allocator.malloc(Val.class)) {
			ptr.deref().val(99);
			Pointer<Val> clone = ptr.clone();

			assertEquals(ptr.address(), clone.address());
			assertEquals(ptr.deref().val(), clone.deref().val());
		}
	}

	/* Aayush
	This method allocates the val struct with pointer ptr and checks to make sure that ptr begins with "0x".
	*/
	@Test
	public void shouldPrintAddressWithToString() {
		try(Pointer<Val> ptr = allocator.malloc(Val.class)) {
			assertTrue(ptr.toString().startsWith("0x"));
		}
	}
}
