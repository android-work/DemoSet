package com.android.work.demoset.design.create.factory

import android.util.Log

class ProductA:AbstractProduct() {
    override fun getProduct() {
        print("生产产品A")
    }
}