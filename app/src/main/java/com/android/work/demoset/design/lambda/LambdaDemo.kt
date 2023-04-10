package com.android.work.demoset.design.lambda

class LambdaDemo {

    /**
     * 声明函数类型
     * (a:Int,b:String) -> Unit
     */
    val faction : (a:Int,b:String) -> Unit = {a,b -> print("a:$a  b:$b")}

    private fun funcInt(function:(a:Int,b:Int) -> Int):Int{
        return function.invoke(3,5)
    }

    private fun funcStr(function: (a:Int) -> String):String{
        return function(10)
    }

    fun test(){
        faction(1,"2")

        funcInt(function = {a,b -> if (a>b) a else b})
        // lambda表达式是函数最后一个实参，可以放到（）外
        funcInt(){a,b -> a + b}
        // 如果lambda表达式是函数唯一的实参，可以省去（）
        funcInt{a,b -> a -b}
        // 如果lambda表达式中只有一个参数，参数名可以省去，默认有it
        funcStr { it.toString() }

    }
}