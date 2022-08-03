package com.example.test.di.mainActivtiy



import com.example.test.di.InfoScope
import com.example.test.presentation.infoFragment.InfoFragment
import dagger.Component

@InfoScope
@Component(
    dependencies = [MainActvitityComponent::class],
)
interface InfoFragmentComponent {
    fun inject(fragment: InfoFragment)
    @Component.Factory
    interface Factory {
        fun create(applicationComponent: MainActvitityComponent): InfoFragmentComponent
    }
}

@InfoScope
@Component(
    dependencies = [MainActvitityComponentTest::class],
)
interface InfoFragmentComponentTest {
    fun inject(fragment: InfoFragment)
    @Component.Factory
    interface Factory {
        fun create(applicationComponent: MainActvitityComponentTest): InfoFragmentComponentTest
    }
}