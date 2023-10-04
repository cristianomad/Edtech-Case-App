package com.cristianomadeira.ulessontest.domain.model

import com.appmattus.kotlinfixture.decorator.nullability.NeverNullStrategy
import com.appmattus.kotlinfixture.decorator.nullability.nullabilityStrategy
import com.appmattus.kotlinfixture.decorator.optional.AlwaysOptionalStrategy
import com.appmattus.kotlinfixture.decorator.optional.optionalStrategy
import com.appmattus.kotlinfixture.kotlinFixture
import com.cristianomadeira.ulessontest.data.dataSource.local.entity.BookmarkEntity
import com.cristianomadeira.ulessontest.data.dataSource.local.entity.ChapterWithLessonsEntity
import com.cristianomadeira.ulessontest.data.dataSource.local.entity.LessonEntity
import com.cristianomadeira.ulessontest.data.dataSource.local.entity.toEntity
import com.cristianomadeira.ulessontest.data.dataSource.local.entity.toModel
import com.cristianomadeira.ulessontest.data.dataSource.remote.dto.ChapterDto
import com.cristianomadeira.ulessontest.data.dataSource.remote.dto.LessonDto
import com.cristianomadeira.ulessontest.data.dataSource.remote.dto.toModel
import com.google.common.truth.Truth.assertThat
import org.junit.Test

class ModelTest {

    private val fixture = kotlinFixture {
        nullabilityStrategy(NeverNullStrategy)
        optionalStrategy(AlwaysOptionalStrategy)
    }

    @Test
    fun `test map ChapterDto to model`() {
        val chapterDto = fixture<ChapterDto>()
        val chapter = chapterDto.toModel()

        assertThat(chapterDto.id).isEqualTo(chapter.id)
        assertThat(chapterDto.enumeration).isEqualTo(chapter.enumeration)
        assertThat(chapterDto.title).isEqualTo(chapter.title)

        chapterDto.lessons.forEachIndexed { index, lessonDto ->
            assertThat(lessonDto.id).isEqualTo(chapter.lessons[index].id)
            assertThat(lessonDto.title).isEqualTo(chapter.lessons[index].title)
            assertThat(lessonDto.thumbUrl).isEqualTo(chapter.lessons[index].thumbUrl)
            assertThat(lessonDto.videoUrl).isEqualTo(chapter.lessons[index].videoUrl)
        }
    }

    @Test
    fun `test map LessonDto to model`() {
        val lessonDto = fixture<LessonDto>()
        val lesson = lessonDto.toModel()

        assertThat(lessonDto.id).isEqualTo(lesson.id)
        assertThat(lessonDto.title).isEqualTo(lesson.title)
        assertThat(lessonDto.thumbUrl).isEqualTo(lesson.thumbUrl)
        assertThat(lessonDto.videoUrl).isEqualTo(lesson.videoUrl)
    }

    @Test
    fun `test map ChapterWithLessonsEntity to model`() {
        val chapterEntity = fixture<ChapterWithLessonsEntity>()
        val chapter = chapterEntity.toModel()

        assertThat(chapterEntity.chapter.id).isEqualTo(chapter.id)
        assertThat(chapterEntity.chapter.enumeration).isEqualTo(chapter.enumeration)
        assertThat(chapterEntity.chapter.title).isEqualTo(chapter.title)

        chapterEntity.lessons.forEachIndexed { index, lessonEntity ->
            assertThat(lessonEntity.id).isEqualTo(chapter.lessons[index].id)
            assertThat(lessonEntity.title).isEqualTo(chapter.lessons[index].title)
            assertThat(lessonEntity.thumbUrl).isEqualTo(chapter.lessons[index].thumbUrl)
            assertThat(lessonEntity.videoUrl).isEqualTo(chapter.lessons[index].videoUrl)
        }
    }

    @Test
    fun `test map model to ChapterEntity`() {
        val chapter = fixture<Chapter>()
        val chapterEntity = chapter.toEntity()

        assertThat(chapter.id).isEqualTo(chapterEntity.chapter.id)
        assertThat(chapter.enumeration).isEqualTo(chapterEntity.chapter.enumeration)
        assertThat(chapter.title).isEqualTo(chapterEntity.chapter.title)

        chapter.lessons.forEachIndexed { index, lesson ->
            assertThat(lesson.id).isEqualTo(chapterEntity.lessons[index].id)
            assertThat(lesson.title).isEqualTo(chapterEntity.lessons[index].title)
            assertThat(lesson.thumbUrl).isEqualTo(chapterEntity.lessons[index].thumbUrl)
            assertThat(lesson.videoUrl).isEqualTo(chapterEntity.lessons[index].videoUrl)
        }
    }

    @Test
    fun `test map LessonEntity to model`() {
        val lessonsEntity = fixture<LessonEntity>()
        val lesson = lessonsEntity.toModel()

        assertThat(lessonsEntity.id).isEqualTo(lesson.id)
        assertThat(lessonsEntity.title).isEqualTo(lesson.title)
        assertThat(lessonsEntity.thumbUrl).isEqualTo(lesson.thumbUrl)
        assertThat(lessonsEntity.videoUrl).isEqualTo(lesson.videoUrl)
    }

    @Test
    fun `test map model to LessonEntity`() {
        val lesson = fixture<Lesson>()
        val lessonEntity = lesson.toEntity(fixture<Int>())

        assertThat(lesson.id).isEqualTo(lessonEntity.id)
        assertThat(lesson.title).isEqualTo(lessonEntity.title)
        assertThat(lesson.thumbUrl).isEqualTo(lessonEntity.thumbUrl)
        assertThat(lesson.videoUrl).isEqualTo(lessonEntity.videoUrl)
    }

    @Test
    fun `test map BookmarkEntity to model`() {
        val bookmarkEntity = fixture<BookmarkEntity>()
        val bookmark = bookmarkEntity.toModel()

        assertThat(bookmarkEntity.id).isEqualTo(bookmark.id)
        assertThat(bookmarkEntity.content).isEqualTo(bookmark.content)
        assertThat(bookmarkEntity.position).isEqualTo(bookmark.position)
    }

    @Test
    fun `test map model to BookmarkEntity`() {
        val bookmark = fixture<Bookmark>()
        val bookmarkEntity = bookmark.toEntity()

        assertThat(bookmark.id).isEqualTo(bookmarkEntity.id)
        assertThat(bookmark.content).isEqualTo(bookmarkEntity.content)
        assertThat(bookmark.position).isEqualTo(bookmarkEntity.position)
    }
}