package com.attendancehr.core.common.result

import org.junit.Test
import kotlin.test.assertEquals
import kotlin.test.assertFalse
import kotlin.test.assertNull
import kotlin.test.assertTrue

class ResultTest {

    @Test
    fun success_creation_storesDataCorrectly() {
        // Arrange
        val testData = "test data"

        // Act
        val result = Result.Success(testData)

        // Assert
        assertEquals(testData, result.data)
    }

    @Test
    fun success_getOrNull_returnsData() {
        // Arrange
        val testData = "test data"
        val result = Result.Success(testData)

        // Act
        val value = result.getOrNull()

        // Assert
        assertEquals(testData, value)
    }

    @Test
    fun success_isSuccess_returnsTrue() {
        // Arrange
        val result = Result.Success("data")

        // Act & Assert
        assertTrue(result.isSuccess())
    }

    @Test
    fun success_isError_returnsFalse() {
        // Arrange
        val result = Result.Success("data")

        // Act & Assert
        assertFalse(result.isError())
    }

    @Test
    fun success_isLoading_returnsFalse() {
        // Arrange
        val result = Result.Success("data")

        // Act & Assert
        assertFalse(result.isLoading())
    }

    @Test
    fun error_creation_storesExceptionCorrectly() {
        // Arrange
        val testException = Exception("test error")

        // Act
        val result = Result.Error(testException)

        // Assert
        assertEquals(testException, result.exception)
    }

    @Test
    fun error_getOrNull_returnsNull() {
        // Arrange
        val testException = Exception("test error")
        val result = Result.Error(testException)

        // Act
        val value = result.getOrNull()

        // Assert
        assertNull(value)
    }

    @Test
    fun error_isSuccess_returnsFalse() {
        // Arrange
        val result = Result.Error(Exception("error"))

        // Act & Assert
        assertFalse(result.isSuccess())
    }

    @Test
    fun error_isError_returnsTrue() {
        // Arrange
        val result = Result.Error(Exception("error"))

        // Act & Assert
        assertTrue(result.isError())
    }

    @Test
    fun error_isLoading_returnsFalse() {
        // Arrange
        val result = Result.Error(Exception("error"))

        // Act & Assert
        assertFalse(result.isLoading())
    }

    @Test
    fun loading_getOrNull_returnsNull() {
        // Arrange
        val result = Result.Loading

        // Act
        val value = result.getOrNull()

        // Assert
        assertNull(value)
    }

    @Test
    fun loading_isSuccess_returnsFalse() {
        // Arrange
        val result = Result.Loading

        // Act & Assert
        assertFalse(result.isSuccess())
    }

    @Test
    fun loading_isError_returnsFalse() {
        // Arrange
        val result = Result.Loading

        // Act & Assert
        assertFalse(result.isError())
    }

    @Test
    fun loading_isLoading_returnsTrue() {
        // Arrange
        val result = Result.Loading

        // Act & Assert
        assertTrue(result.isLoading())
    }

    @Test
    fun map_onSuccess_appliesTransformation() {
        // Arrange
        val result = Result.Success(5)

        // Act
        val mappedResult = result.map { it * 2 }

        // Assert
        assertTrue(mappedResult is Result.Success)
        assertEquals(10, (mappedResult as Result.Success).data)
    }

    @Test
    fun map_onError_returnsSameError() {
        // Arrange
        val testException = Exception("test error")
        val result = Result.Error(testException)

        // Act
        val mappedResult = result.map { it * 2 }

        // Assert
        assertTrue(mappedResult is Result.Error)
        assertEquals(testException, (mappedResult as Result.Error).exception)
    }

    @Test
    fun map_onLoading_returnsSameLoading() {
        // Arrange
        val result = Result.Loading

        // Act
        val mappedResult = result.map { it * 2 }

        // Assert
        assertTrue(mappedResult is Result.Loading)
    }
}
