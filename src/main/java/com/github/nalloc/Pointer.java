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

package com.github.nalloc;

/**
 * Pointer to a memory address.
 *
 * @author Antti Laisi
 */
public interface Pointer<T> extends AutoCloseable {

	/**
	 * @return Struct referenced by this pointer.
	 */
	
	/* Aayush
	This method returns the struct at a given pointer.
	*/
	T deref();

	/**
	 * @return Memory address pointed to.
	 */
	
	/* Aayush
	This method returns the memory address that a given pointer holds.
	*/
	long address();

	/**
	 * Sets memory address.
	 *
	 * @param New address
	 */
	
	/* Aayush
	This method sets a struct's memory address after being given one.
	*/
	void address(final long address);

	/**
	 * Frees memory that is pointed to by this pointer.
	 */
	
	/* Aayush
	This method clears the memory at a given pointer.
	*/
	void free();

	/**
	 * Returns a shallow clone of a pointer. Cloning is a cheap operation, only the pointer is
	 * cloned and not the data pointed to.
	 *
	 * @return Clone of this pointer
	 */
	
	/* Aayush
	This method clones a pointer after being given one.
	*/
	Pointer<T> clone();

	/**
	 * AutoCloseable support, calls free().
	 */
	
	/* Aayush
	This method calls the free() method and closes any existing resources.
	*/
	@Override
	void close();

}
