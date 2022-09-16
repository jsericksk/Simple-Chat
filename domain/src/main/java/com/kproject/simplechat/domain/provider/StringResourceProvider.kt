package com.kproject.simplechat.domain.provider

interface StringResourceProvider {
    fun getString(stringResId: Int, formatArgs: Array<Any>? = null): String
}