package com.cristianomadeira.ulessontest.data.dataSource.local

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.PreferenceDataStoreFactory
import androidx.datastore.preferences.core.Preferences
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.cristianomadeira.ulessontest.data.dataSource.local.entity.MilestoneEntity
import com.google.common.truth.Truth
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.test.TestScope
import kotlinx.coroutines.test.UnconfinedTestDispatcher
import kotlinx.coroutines.test.runTest
import org.junit.Rule
import org.junit.Test
import org.junit.rules.TemporaryFolder
import org.junit.runner.RunWith

@OptIn(ExperimentalCoroutinesApi::class)
@RunWith(AndroidJUnit4::class)
class MilestoneDataSourceTest {
    @get:Rule
    val tempFolder: TemporaryFolder = TemporaryFolder.builder().assureDeletion().build()

    private val testDispatcher = UnconfinedTestDispatcher()
    private val testScope = TestScope(testDispatcher + Job())

    private val testDataStore: DataStore<Preferences> = PreferenceDataStoreFactory.create(
        scope = testScope,
        produceFile = { tempFolder.newFile("milestone.preferences_pb") }
    )

    private val milestoneDataSource: MilestoneDataSource = MilestoneDataSourceImpl(testDataStore)

    @Test
    fun test_save_milestone_to_data_store() {
        val test = MilestoneEntity(
            date = "02-03-2023",
            isAchievementSeen = false
        )

        testScope.runTest {
            milestoneDataSource.setMilestone(test)
            val subject = milestoneDataSource.getMilestone().first()

            Truth.assertThat(subject?.date).isEqualTo(test.date)
            Truth.assertThat(subject?.isAchievementSeen).isEqualTo(test.isAchievementSeen)
        }
    }
}