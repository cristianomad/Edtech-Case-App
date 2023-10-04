package com.cristianomadeira.ulessontest.data.dataSource.remote

import android.util.Log
import com.cristianomadeira.ulessontest.data.dataSource.remote.client.HttpClientProvider
import com.cristianomadeira.ulessontest.data.dataSource.remote.dto.ChapterDto
import com.google.common.truth.Truth.assertThat
import io.ktor.client.engine.mock.MockEngine
import io.ktor.client.engine.mock.respond
import io.ktor.http.HttpHeaders
import io.ktor.http.HttpStatusCode
import io.ktor.http.headersOf
import io.ktor.utils.io.ByteReadChannel
import io.mockk.every
import io.mockk.mockkStatic
import kotlinx.coroutines.Job
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.TestScope
import kotlinx.coroutines.test.runTest
import kotlinx.serialization.json.Json
import org.junit.Before
import org.junit.Test

class ChapterDataSourceTest {

    private val testDispatcher = StandardTestDispatcher()
    private val testScope = TestScope(testDispatcher + Job())

    @Before
    fun prepare() {
        mockkStatic(Log::class)
        every { Log.v(any(), any()) } returns 0
        every { Log.d(any(), any()) } returns 0
        every { Log.i(any(), any()) } returns 0
        every { Log.e(any(), any()) } returns 0
    }

    @Test
    fun `Test api successful request`() {
        val json = "[\n" +
                "  {\n" +
                "    \"id\": 1,\n" +
                "    \"enumeration\": 1,\n" +
                "    \"title\": \"Biology\",\n" +
                "    \"lessons\": [\n" +
                "      {\n" +
                "        \"id\": 1,\n" +
                "        \"title\": \"Recognizing Things: Living vs Non-Living\",\n" +
                "        \"thumbUrl\": \"https://ik.imagekit.io/lhpsfdvnd/image-1.png\",\n" +
                "        \"videoUrl\": \"https://ik.imagekit.io/lhpsfdvnd/recognizing-living-things-living-vs-non-living.mp4\"\n" +
                "      },\n" +
                "      {\n" +
                "        \"id\": 2,\n" +
                "        \"title\": \"Recognizing Living Things: Is it alive?\",\n" +
                "        \"thumbUrl\": \"https://ik.imagekit.io/lhpsfdvnd/image-2.png\",\n" +
                "        \"videoUrl\": \"https://ik.imagekit.io/lhpsfdvnd/reconizing-things-is-it-alive.mp4\"\n" +
                "      },\n" +
                "      {\n" +
                "        \"id\": 3,\n" +
                "        \"title\": \"Kingdom or Domain?\",\n" +
                "        \"thumbUrl\": \"https://ik.imagekit.io/lhpsfdvnd/image-5.png\",\n" +
                "        \"videoUrl\": \"https://ik.imagekit.io/lhpsfdvnd/kingdom-or-domain.mp4\"\n" +
                "      },\n" +
                "      {\n" +
                "        \"id\": 4,\n" +
                "        \"title\": \"The Three Domains of Life\",\n" +
                "        \"thumbUrl\": \"https://ik.imagekit.io/lhpsfdvnd/image-3.png\",\n" +
                "        \"videoUrl\": \"https://ik.imagekit.io/lhpsfdvnd/the-three-domains-of-life.mp4\"\n" +
                "      },\n" +
                "      {\n" +
                "        \"id\": 5,\n" +
                "        \"title\": \"Calculations on Magnifications & Resolution\",\n" +
                "        \"thumbUrl\": \"https://ik.imagekit.io/lhpsfdvnd/image-4.png\",\n" +
                "        \"videoUrl\": \"https://ik.imagekit.io/lhpsfdvnd/calculations-on-magnifications-and-resolutions.mp4\"\n" +
                "      }\n" +
                "    ]\n" +
                "  }\n" +
                "]"

        val expected = Json.decodeFromString<List<ChapterDto>>(json)

        val mockEngine = MockEngine {
            respond(
                content = ByteReadChannel(json),
                status = HttpStatusCode.OK,
                headers = headersOf(HttpHeaders.ContentType, "application/json")
            )
        }

        testScope.runTest {
            val httpClient = HttpClientProvider(mockEngine)
            val dataSource = ChapterDataSourceImpl(httpClient)
            val response = dataSource.fetchChapters()

            response.onSuccess { chapters ->
                chapters.forEachIndexed { i, chapter ->
                    assertThat(chapter.id).isEqualTo(expected[i].id)
                    assertThat(chapter.enumeration).isEqualTo(expected[i].enumeration)
                    assertThat(chapter.title).isEqualTo(expected[i].title)

                    chapter.lessons.forEachIndexed { j, lesson ->
                        assertThat(lesson.id).isEqualTo(expected[i].lessons[j].id)
                        assertThat(lesson.title).isEqualTo(expected[i].lessons[j].title)
                        assertThat(lesson.thumbUrl).isEqualTo(expected[i].lessons[j].thumbUrl)
                        assertThat(lesson.videoUrl).isEqualTo(expected[i].lessons[j].videoUrl)
                    }
                }
            }
        }
    }
}