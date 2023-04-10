package com.android.work.demoset.design.create.factory

class FactoryB : AbstractFactory() {
    override fun createProduct():AbstractProduct {
        return ProductB()
    }
}