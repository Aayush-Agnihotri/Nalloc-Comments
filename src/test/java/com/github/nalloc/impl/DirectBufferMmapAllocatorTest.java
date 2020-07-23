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
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import java.io.File;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;

import org.junit.After;
import org.junit.Test;

import com.github.nalloc.Array;
import com.github.nalloc.MmapAllocator;
import com.github.nalloc.Struct;
import com.github.nalloc.Struct.Field;
import com.github.nalloc.Struct.Type;

/**
 * Unit tests for {@link DirectBufferMmapAllocator}.
 *
 * @author Antti Laisi
 */
public class DirectBufferMmapAllocatorTest {

	final MmapAllocator allocator = MmapAllocator.Factory.create(MyMappedStruct.class);
	File file;

	/* Aayush
	This method allocates the MyMappedStruct struct as an array named "array", sets struct equal to the first element of array, changes struct to 1, and then checks if struct equals 1, if file object exists, and if 10 * the size of struct equals the file length.
	*/
	@Test
	public void shouldMemoryMapFile() throws IOException {
		try(Array<MyMappedStruct> array = allocator.mmap(file(), 10, MyMappedStruct.class)) {
			MyMappedStruct struct = array.get(0);
			struct.id(1);

			assertEquals(1, struct.id());
			assertTrue(file.exists());
			assertEquals(10 * struct.getSize(), file.length());
		}
	}

	/* Aayush
	This method allocates the MyMappedStruct struct as an array named "array", sets struct equal to the first element of array, creates buffer as a byte array of array, sets the first element of buffer as 77, and checks if 77 equals struct's first field.
	*/
	@Test
	public void shouldAccessStructAsByteBuffer() throws IOException {
		try(Array<MyMappedStruct> array = allocator.mmap(file(), 1, MyMappedStruct.class)) {
			MyMappedStruct struct = array.get(0);
			ByteBuffer buffer = allocator.toBytes(array);
			assertNotNull(buffer);

			buffer.putLong(0, 77);
			assertEquals(77, struct.id());
		}
	}

	/* Aayush
	This method creates a buffer with a capacity of 128 bytes named "buffer", puts 123 in the buffer, allocates the MyMappedStruct struct as an array named "array", and checks if 123 equals the MyMappedStruct field.
	*/
	@Test
	public void shouldMemoryMapBuffer() {
		ByteBuffer buffer = ByteBuffer.allocateDirect(128);
		buffer.order(ByteOrder.nativeOrder());
		buffer.putLong(123);

		Array<MyMappedStruct> array = allocator.mmap(buffer, MyMappedStruct.class);
		assertEquals(123, array.deref().id());
	}

	/* Aayush
	This method allocates the MyMappedStruct struct as an array named "array", sets the 10th element of the array to 19, creates a buffered array of array named bytes, and then checks if 10th element of array and 9 times the size of bytes equals 19. 
	*/
	@Test
	public void shoulMemoryMapAnonymousBuffer() {
		try(Array<MyMappedStruct> array = allocator.mmap(10, MyMappedStruct.class)) {
			array.get(9).id(19);
			ByteBuffer bytes = allocator.toBytes(array);

			assertEquals(19, array.get(9).id());
			assertEquals(19, bytes.getLong(9 * (int) array.deref().getSize()));
		}
	}

	@Test(expected=IllegalArgumentException.class)
	public void shouldRejectCastingIndirectBuffer() throws IOException {
		allocator.mmap(ByteBuffer.allocate(8), MyMappedStruct.class);
	}

	@Test(expected=IllegalArgumentException.class)
	public void shouldRejectCastingBufferInWrongOrder() throws IOException {
		allocator.mmap(ByteBuffer.allocateDirect(8), MyMappedStruct.class);
	}

	/* Aayush
	This method deletes file if the object's value is not null.
	*/
	@After
	public void cleanup() {
		if(file != null) {
			file.delete();
		}
	}

	File file() throws IOException {
		return file = File.createTempFile(getClass().getSimpleName(), ".map");
	}

	/* Aayush
	This method creates the struct named MyMappedStruct with its long field.
	*/
	@Struct({
		@Field(name="id", type=Type.LONG) })
	static interface MyMappedStruct {
		long id();
		void id(final long id);
		long getSize();
	}
}
