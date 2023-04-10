package com.android.work.demoset.design.create.factory

class FactoryA : AbstractFactory() {
    override fun createProduct():AbstractProduct {
        return ProductA()
    }
}