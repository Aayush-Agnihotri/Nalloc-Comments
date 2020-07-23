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

import java.io.File;
import java.io.IOException;

import org.junit.Test;

import com.github.nalloc.Array;
import com.github.nalloc.MmapAllocator;

/**
 * Unit tests for {@link MmapArray}.
 */
public class MmapArrayTest {

	final MmapAllocator allocator = MmapAllocator.Factory.create(Val.class);

	/* Aayush
	This method allocates space for a file of 5 structs and creates array as an array of the structs. It then:
	1.) Creates clone, which is a copy of the pointer to array
	2.) Creates v1 as the first element of array
	3.) Sets the v1.val field equal to 123
	4.) Creates v2 as the second element of clone
	5.) Sets the v2.val field equal to 456
	
	6.) Checks if the address of the original array equals the cloned array
	7.) Checks if the size of the original array equals the cloned array 
	8.) Checks if the v1.val field does not equal the v2.val field
	9.) Checks if the v1.val field equals the cloned field
	10.) Deletes the file object
	*/
	@Test
	public void shouldCloneArray() throws IOException {
		File file = File.createTempFile(getClass().getSimpleName(), ".map");
		try(Array<Val> array = allocator.mmap(file, 5, Val.class)) {
			Array<Val> clone = array.clone();
			Val v1 = array.get(0);
			v1.val(123);
			Val v2 = clone.get(1);
			v2.val(456);

			assertEquals(array.address(), clone.address());
			assertEquals(array.size(), clone.size());
			assertTrue(v1.val() != v2.val());
			assertEquals(v1.val(), clone.get(0).val());
		}
		file.delete();
	}

}
