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
import com.github.nalloc.Pointer;
import com.github.nalloc.Struct;
import com.github.nalloc.Struct.Field;
import com.github.nalloc.Struct.Type;

/**
 * Unit tests for {@link NativeStruct}.
 *
 * @author Antti Laisi
 */
public class NativeStructTest {

	final NativeHeapAllocator allocator = NativeHeapAllocator.Factory.create(NestedStruct.class);

	/* Aayush
	This method allocates space for the NestedStruct struct and creates a clone of the NestedStruct pointer named clone. It then:
	1.) Sets the int field of the nested Val struct to 1 
	2.) Sets the first element of the array field in the NestedStruct struct to 2
	
	3.) Checks if the original struct and cloned struct match
	4.) Checks if the nested int field equals the cloned int field
	5.) Checks if the first element of the original array equals the first element of the cloned array
	*/
	@Test
	public void shouldCloneNestedFields() {
		try(Pointer<NestedStruct> ptr = allocator.malloc(NestedStruct.class)) {
			Pointer<NestedStruct> clone = ptr.clone();
			ptr.deref().nested().val(1);
			ptr.deref().array().get(1).val(2);

			assertTrue(ptr.deref() != clone.deref());
			assertEquals(ptr.deref().nested().val(), clone.deref().nested().val());
			assertEquals(ptr.deref().array().get(1).val(), clone.deref().array().get(1).val());
		}
	}

	/* Aayush
	This method creates a struct named NestedStruct with a nested struct field and an array field.
	*/
	@Struct({
		@Field(name="nested", type=Type.STRUCT, struct=Val.class),
		@Field(name="array", type=Type.STRUCT, struct=Val.class, len=2) })
	static interface NestedStruct {
		Val nested();
		Array<Val> array();
	}
}
