package com.ru.foundation

import com.ru.foundation.model.Repository

interface BaseApplication {

    val repositoryDependencies: List<Repository>

}