package com.music.app.listener

import com.music.app.model.Category

interface IOnAdminManagerCategoryListener {
    fun onClickUpdateCategory(category: Category)
    fun onClickDeleteCategory(category: Category)
    fun onClickDetailCategory(category: Category)
}