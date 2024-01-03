package com.alz19.storyapp2.view.main

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.paging.AsyncPagingDataDiffer
import androidx.paging.PagingData
import androidx.paging.PagingSource
import androidx.paging.PagingState
import androidx.recyclerview.widget.ListUpdateCallback
import com.alz19.storyapp2.DataDummy
import com.alz19.storyapp2.MainDispatcherRule
import com.alz19.storyapp2.getOrAwaitValue
import com.alz19.storyapp2.helper.adapter.StoryListAdapter
import com.alz19.storyapp2.model.entity.ListStoryItem
import com.alz19.storyapp2.repository.story.StoryRepository
import com.alz19.storyapp2.repository.token.TokenRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runTest
import org.junit.Assert
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mock
import org.mockito.Mockito
import com.alz19.storyapp2.helper.utils.Result
import org.mockito.junit.MockitoJUnitRunner

@ExperimentalCoroutinesApi
@RunWith(MockitoJUnitRunner::class)
class MainViewModelTest {
    @get:Rule
    val instantExecutorRule = InstantTaskExecutorRule()

    @get:Rule
    val mainDispatcherRule = MainDispatcherRule()

    @Mock
    private lateinit var storyRepository: StoryRepository

    @Mock
    private lateinit var tokenRepository: TokenRepository

    @Test
    fun `when Get Story Should Not Null and Return Data`() = runTest {
        val dummyStory = DataDummy.generateDummyStoryResponse()
        val data: PagingData<ListStoryItem> = StoryPagingSource.snapshot(dummyStory)
        val expectedStory = MutableLiveData<PagingData<ListStoryItem>>()
        expectedStory.value = data

        Mockito.`when`(tokenRepository.getSession()).thenReturn("your_token")
        Mockito.`when`(storyRepository.getAllStory("your_token")).thenReturn(expectedStory)

        val mainViewModel = MainViewModel(storyRepository, tokenRepository)
        mainViewModel.getAllStoryPaging()
        val actualStory: Result<LiveData<PagingData<ListStoryItem>>> = mainViewModel.story.getOrAwaitValue()

        assert(actualStory is Result.Success) { "Result is not a success" }

        val pagingData: PagingData<ListStoryItem> = (actualStory as Result.Success).data.getOrAwaitValue()
        val differ = AsyncPagingDataDiffer(
            diffCallback = StoryListAdapter.DIFF_CALLBACK,
            updateCallback = noopListUpdateCallback,
            workerDispatcher = Dispatchers.Main,
        )

        differ.submitData(pagingData)

        Assert.assertNotNull(differ.snapshot())
        Assert.assertEquals(dummyStory.size, differ.snapshot().size)
        Assert.assertEquals(dummyStory[0], differ.snapshot()[0])
    }


    @Test
    fun `when Get Story Empty Should Return No Data`() = runTest {
        val data: PagingData<ListStoryItem> = PagingData.from(emptyList())
        val expectedStory = MutableLiveData<PagingData<ListStoryItem>>()
        expectedStory.value = data

        Mockito.`when`(tokenRepository.getSession()).thenReturn("your_token")
        Mockito.`when`(storyRepository.getAllStory("your_token")).thenReturn(expectedStory)

        val mainViewModel = MainViewModel(storyRepository, tokenRepository)
        mainViewModel.getAllStoryPaging()
        val actualStory: Result<LiveData<PagingData<ListStoryItem>>> = mainViewModel.story.getOrAwaitValue()

        assert(actualStory is Result.Success) { "Result is not a success" }

        val pagingData: PagingData<ListStoryItem> = (actualStory as Result.Success).data.getOrAwaitValue()
        val differ = AsyncPagingDataDiffer(
            diffCallback = StoryListAdapter.DIFF_CALLBACK,
            updateCallback = noopListUpdateCallback,
            workerDispatcher = Dispatchers.Main,
        )

        differ.submitData(pagingData)

        Assert.assertEquals(0, differ.snapshot().size)
    }
}

val noopListUpdateCallback = object : ListUpdateCallback {
    override fun onInserted(position: Int, count: Int) {}
    override fun onRemoved(position: Int, count: Int) {}
    override fun onMoved(fromPosition: Int, toPosition: Int) {}
    override fun onChanged(position: Int, count: Int, payload: Any?) {}
}

class StoryPagingSource : PagingSource<Int, LiveData<List<ListStoryItem>>>() {
    companion object {
        fun snapshot(items: List<ListStoryItem>): PagingData<ListStoryItem> {
            return PagingData.from(items)
        }
    }
    override fun getRefreshKey(state: PagingState<Int, LiveData<List<ListStoryItem>>>): Int {
        return 0
    }
    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, LiveData<List<ListStoryItem>>> {
        return LoadResult.Page(emptyList(), 0, 1)
    }
}