package com.yudhakautsar.storyapp.domain.usecase

import com.yudhakautsar.storyapp.base.Resource

interface UseCase<in Params, out Result> {
    suspend operator fun invoke(params: Params): Resource<Result>
}

