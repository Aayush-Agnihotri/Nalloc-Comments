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

import com.github.nalloc.Array;
import com.github.nalloc.NativeHeapAllocator;

/**
 * Unit tests for {@link HeapArray}.
 *
 * @author Antti Laisi
 */
public class HeapArrayTest {

	final NativeHeapAllocator allocator = NativeHeapAllocator.Factory.create(Val.class);

	/* Aayush
	This method allocates two arrays, s1 and s2, switches their memory addresses, and checks if the fields previously set stay the same.
	*/
	@Test
	public void shouldSetArrayAddress() {
		try(Array<Val> s1 = allocator.calloc(2, Val.class);
				Array<Val> s2 = allocator.calloc(2, Val.class)) {
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
	This method allocates the val array as array, clones array and names it clone, sets the first element of array to 123, and then makes sure that the original array's address, size, and first element equal the clone's.
	*/
	@Test
	public void shouldCloneArray() {
		try(Array<Val> array = allocator.calloc(2, Val.class)) {
			Array<Val> clone = array.clone();
			array.get(1).val(123);

			assertEquals(array.address(), clone.address());
			assertEquals(array.size(), clone.size());
			assertEquals(array.get(1).val(), clone.get(1).val());
		}
	}

	/* Aayush
	This method allocates the val array as ptr and checks if ptr begins with "0x".
	*/
	@Test
	public void shouldPrintAddressWithToString() {
		try(Array<Val> ptr = allocator.calloc(1, Val.class)) {
			assertTrue(ptr.toString().startsWith("0x"));
		}
	}

	/* Aayush
	This method allocates the val array as array, sets the array's first element to 9, clears the element, and checks if the first element is equal to 0.
	*/
	@Test
	public void shouldClearIndex() {
		try(Array<Val> array = allocator.calloc(1, Val.class)) {
			array.get(0).val(9);
			array.clear(0);
			assertEquals(0, array.get(0).val());
		}
	}

}
