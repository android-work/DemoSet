package com.android.work.demoset.algorithm

import android.util.Log
import android.util.SparseIntArray
import androidx.core.util.contains
import androidx.core.util.forEach
import kotlin.math.pow


fun algorithm() {
    Thread(Runnable {
        // 冒泡排序：相邻之间比较，交换，将最大的数放后面
        moPaoSort()
        // 选择排序：每次轮询，找出最大的/最小的
        choseSort()
        // 基数排序：先以个位数为基准，比较相同的个位数放一起，再汇总，再以十位数为基准，依次类推至最大位数，再汇总即ok
        baseSort()
        // 计数排序：不适合较多且跨度太大的数列，需要有个空数列，然后数列索引对应数值，数列元素表示数值个数
        countSort()
        // 快速排序：随机定义一个基数(默认第一个)，从首尾开始遍历，找比基数大/小的进行大小交换，直至2个指针相遇，将基数插入对应位置中。
        // 再以相遇的点为分界点，重复上面遍历步骤，直至每个域个数为1
        fastSort()
        // 插入排序：以第一个数为固定点，然后往后遍历元素，找到元素再有序数列中的合适位置
        insertSort()
        // 归并排序：将已排好序的数列与相邻的数进行比较排序，即为一个插入排序
        mergeSort()
        // 将数列中的个位、十位等分域，然后每个域内进行排序，再汇总
        tongSort()
        // 随机设置每间隔固定数的元素为一组，将其进行排序后再还原到愿数列的位置，然后再缩减间隔数，重复以上步骤，直至间隔数为1
        xiErSort()
    }).start()
}


/**
 * 冒泡排序(比较相邻两个数)
 *
 * 第一遍(循环n-1遍)：比较出最大的元素，放倒最后
 * 第二遍(循环n-2遍)：筛选出次大的元素
 * 。。。
 * 第n-1遍：筛选出最小元素
 */
fun moPaoSort() {
    val tempList = arrayListOf<Int>(
        78,
        23,
        2,
        444,
        90,
        2,
        89,
        22,
        46,
        20,
        42,
        3,
        89,
        23,
        11,
        57,
        90,
        25,
        888,
        8888,
        9999
    )
    Log.e("Algorithm_moPaoSort", "---------------->冒泡原始列表：$tempList")
    for (i in 0 until tempList.size) {
        for (j in 0 until (tempList.size - 1 - i)) {
            // 取相邻两个数进行比较，较大的在右边
            if (tempList[j] > tempList[j + 1]) {
                val temp = tempList[j]
                tempList[j] = tempList[j + 1]
                tempList[j + 1] = temp
            }
        }
        Log.e("Algorithm_moPaoSort", "       ---------------->第${i}遍循环排序：${tempList}")
    }
    Log.w("Algorithm_moPaoSort", "---------------->冒泡列表：$tempList")

    Log.e("Algorithm_moPaoSort", "-------------------->冒泡end<----------------------\n\n")
}

/**
 * 选择排序(先选出一个最大/最小的数，依次选择)
 */
fun choseSort() {
    val tempList = arrayListOf<Int>(
        78,
        23,
        2,
        444,
        90,
        2,
        89,
        22,
        46,
        20,
        42,
        3,
        89,
        23,
        11,
        57,
        90,
        25,
        888,
        8888,
        9999
    )
    Log.d("Algorithm_choseSort", "---------------->选择原始列表：$tempList")
    for (i in 0 until tempList.size) {
        for (j in (i + 1) until (tempList.size)) {
            //取第一个与后面元素比较，找出最小第放倒前面
            if (tempList[i] > tempList[j]) {
                val temp = tempList[i]
                tempList[i] = tempList[j]
                tempList[j] = temp
            }
        }
        Log.d("Algorithm_choseSort", "      ------->第${i}遍循环排序：${tempList}")
    }
    Log.w("Algorithm_choseSort", "---------------->选择列表：$tempList")
    Log.d("Algorithm_choseSort", "-------------------->选择end<----------------------\n\n")
}

/**
 * 插入排序(固定第一个，然后挨个将后面的元素往前比较，找到一个合适的位置插入)
 *
 * 注意：是插入不是交换
 */
fun insertSort() {
    val tempList = arrayListOf<Int>(
        78,
        23,
        2,
        444,
        90,
        2,
        89,
        22,
        46,
        20,
        42,
        3,
        89,
        23,
        11,
        57,
        90,
        25,
        888,
        8888,
        9999
    )
    Log.d("Algorithm_insertSort", "---------------->插入原始列表：$tempList")
    for (i in 1 until tempList.size) {
        if (tempList[i - 1] < tempList[i]) {
            continue
        }
        for (j in (i - 1) downTo 0) {
            if (tempList[i] < tempList[j]) {
                if (j == 0) {
                    val element = tempList.removeAt(i)
                    tempList.add(j, element)
                    break
                }
                continue
            } else {
                val element = tempList.removeAt(i)
                tempList.add(j + 1, element)
                break
            }
        }
        Log.d("Algorithm_insertSort", "     ------->第${i}遍循环排序：${tempList}")
    }
    Log.w("Algorithm_insertSort", "---------------->插入列表：$tempList")

    Log.d("Algorithm_insertSort", "-------------------->插入end<----------------------\n\n")
}

/**
 * 快速排序(随机选取一个数为基准，从后找比基准数小的，从前找比基准数大的，二者交换位置，
 * 目的是为了将小于基数的放左边，大于基数的放右边，
 * 直至二者指针相遇，说明所在索引位是基数添加位，将数组分割，再重复原先步骤)
 */
fun fastSort() {
    val tempList = arrayListOf<Int>(
        78,
        23,
        2,
        444,
        90,
        2,
        89,
        22,
        46,
        20,
        42,
        3,
        89,
        23,
        11,
        57,
        90,
        25,
        888,
        8888,
        9999
    )
    Log.e("Algorithm_fastSort", "---------------->快速原始列表：$tempList")

    fastMethod(tempList, 0, tempList.size - 1)
    Log.w("Algorithm_fastSort", "----------------->快速排序完成后列表:${tempList}")
    Log.d("Algorithm_fastSort", "-------------------->快速end<----------------------\n\n")
}

private fun fastMethod(tempList: MutableList<Int>, startIndex: Int, endIndex: Int) {

    if (tempList.isEmpty())
        return

    var leftIndex = startIndex // 第一次从0开始
    var rightIndex = endIndex
    val baseValue = tempList.first()
    /*if (baseValue == null){
        return
    }*/

    while (leftIndex < rightIndex) {

        // 移动右指针，寻找小于基数的，找到则放到leftIndex位置，当前位置空闲，待存放左指针目标数
        for (i in rightIndex downTo leftIndex) {
            // 左右指针相遇，结束循环
            if (i == leftIndex) {
                rightIndex = i
                break
            }
            if (tempList[i] < baseValue) {
                tempList[leftIndex] = tempList[i]
                rightIndex = i
                break
            }
        }

        // 移动左指针，寻找大于基数的，找到则放到rightIndex位置，当前位置空闲，待存放右指针目标数
        for (i in leftIndex..rightIndex) {
            // 左右指针相遇，结束循环
            if (i == rightIndex) {
                leftIndex = i
                break
            }
            if (tempList[i] > baseValue) {
                tempList[rightIndex] = tempList[i]
                leftIndex = i
                break
            }
        }

        if (rightIndex == leftIndex) {
            tempList[leftIndex] = baseValue

            // 以baseValue新的位置划分2个区域，再重复上面逻辑
            val lowList = tempList.subList(0, leftIndex)
            Log.e("Algorithm_fastMethod", "  ------->lowList:$lowList")
            fastMethod(lowList, 0, lowList.size - 1)
            if (tempList.size != leftIndex + 1) {
                val highList = tempList.subList(leftIndex + 1, tempList.size)
                Log.e("Algorithm_fastMethod", "  ------->highList:$highList")
                fastMethod(highList, 0, highList.size - 1)
            }
        }
        Log.e("Algorithm_fastMethod", "------->tempList:$tempList")
    }
}

/**
 * 堆排序
 */
fun heapSort() {
    val tempList = arrayListOf<Int>(
        78,
        23,
        2,
        444,
        90,
        2,
        89,
        22,
        46,
        20,
        42,
        3,
        89,
        23,
        11,
        57,
        90,
        25,
        888,
        8888,
        9999
    )

    Log.d("Algorithm_heapSort", "---------------->堆原始列表：$tempList")


    Log.d("Algorithm_heapSort", "-------------------->堆end<----------------------\n\n")
}

/**
 * 归并排序(先将数列拆分，直至拆分到单个元素为止，然后在进行合并，两两合并并排好顺序，然后在跟下一个数合并排序，以此类推)
 */
fun mergeSort() {
    val tempList = arrayListOf<Int>(
        78,
        23,
        2,
        444,
        90,
        2,
        89,
        22,
        46,
        20,
        42,
        3,
        89,
        23,
        11,
        57,
        90,
        25,
        888,
        8888,
        9999
    )
    Log.e("Algorithm_mergeSort", "---------------->归并原始列表：$tempList")
    for (i in 1 until tempList.size) {
        if (tempList[i] > tempList[i - 1]) {
            continue
        } else {
            for (j in (i - 1) downTo 0) {
                if (tempList[i] < tempList[j]) {
                    if (j == 0) {
                        val element = tempList.removeAt(i)
                        tempList.add(j, element)
                        break
                    }
                }else{
                    val element = tempList.removeAt(i)
                    tempList.add(j + 1, element)
                    break
                }
            }
        }
        Log.e("Algorithm_mergeSort", "      ---------------->归并列表：$tempList")
    }
    Log.w("Algorithm_mergeSort", "----------------->归并后数列：$tempList")
    Log.e("Algorithm_mergeSort", "-------------------->归并end<----------------------\n\n")
}

/**
 * 希尔排序(对数据进行分组，按每间隔n个数据为一组，对每组进行排序，再缩减间隔数一般为n/2，直至为1)
 */
fun xiErSort() {
    val tempList = arrayListOf<Int>(
        78,
        23,
        2,
        444,
        90,
        2,
        89,
        22,
        46,
        20,
        42,
        3,
        89,
        23,
        11,
        57,
        90,
        25,
        888,
        8888,
        9999
    )
    Log.e("Algorithm_xiErSort", "---------------->希尔原始列表：$tempList")
    xiErMethod(tempList, 4)
    Log.w("Algorithm_xiErSort", "---------------->希尔列表：$tempList")
    Log.e("Algorithm_xiErSort", "-------------------->希尔end<----------------------\n\n")
}

private fun xiErMethod(tempList: ArrayList<Int>, n: Int) {
    // 间隔循环
    for(i in 0 until n){
        for(j in 0 until tempList.size){
            val index = (n + 1) * j + i
            if (index >= tempList.size)
                break
            // 比较index之前的元素大小
            for (z in j downTo 0){
                val preIndex = (n + 1) * z + i
                if (tempList[preIndex] <= tempList[index])
                    continue
                else{
                    val temp = tempList[preIndex]
                    tempList[preIndex] = tempList[index]
                    tempList[index] = temp
                }
            }
        }
    }
    Log.e("Algorithm_xiErSort", "       ---------------->希尔排序数列：$tempList")
    if (n != 0) {
        xiErMethod(tempList, n / 2)
    }
}

/**
 * 计数排序(计数排序，需要提前先创建好空数组，然后把数据按下表存放出现次数，最后把数据在按顺序遍历)
 *
 * 注意：不支持数据量太大、数据跨度太大
 */
fun countSort() {
    val tempList = arrayListOf<Int>(
        78,
        23,
        2,
        444,
        90,
        2,
        89,
        22,
        46,
        20,
        42,
        3,
        89,
        23,
        11,
        57,
        90,
        25,
        888,
        8888,
        9999
    )

    Log.d("Algorithm_countSort", "---------------->计数原始列表：$tempList")
    val sparseArray = SparseIntArray()
    tempList.forEach {
        if (sparseArray.contains(it)) {
            sparseArray.put(it, sparseArray.get(it) + 1)
        } else {
            sparseArray.put(it, 1)
        }
    }

    tempList.clear()
    sparseArray.forEach { key, value ->
        for (i in 1..value) {
            tempList.add(key)
        }
    }
    Log.w("Algorithm_countSort", "---------------->计数后列表：$tempList")

    Log.d("Algorithm_countSort", "-------------------->计数end<----------------------\n\n")
}

/**
 * 桶排序(按照数值的位数进行分组，然后每组内排序)
 */
fun tongSort() {
    val tempList = arrayListOf<Int>(
        78,
        23,
        2,
        444,
        90,
        2,
        89,
        22,
        46,
        20,
        42,
        3,
        89,
        23,
        11,
        57,
        90,
        25,
        888,
        8888,
        9999
    )

    Log.d("Algorithm_tongSort", "---------------->桶原始列表：$tempList")
    val sparseArray = CustomSparseArray<ArrayList<Int>>()
    tempList.forEach {
        val value = if (it.toString().contains(".")) {
            it.toString().split(".")[0]
        } else {
            it.toString()
        }
        if (sparseArray.containers(value.length)) {
            val tongList = sparseArray.get(value.length)
            for (i in (tongList.size - 1) downTo 0) {
                if (tongList.last() < it){
                    tongList.add(it)
                    break
                }
                if (tongList[i] > it) {
                    if (i == 0){
                        tongList.add(0,it)
                        break
                    }
                    continue
                } else {
                    tongList.add(i + 1, it)
                    break
                }
            }
        } else {
            sparseArray.put(value.length, arrayListOf(it))
        }
    }
    tempList.clear()
    sparseArray.forEach { _, list ->
        tempList.addAll(list)
    }
    Log.w("Algorithm_tongSort", "---------------->桶列表：$tempList")

    Log.d("Algorithm_tongSort", "-------------------->桶end<----------------------\n\n")
}

/**
 * 基数排序(先找出最大位数，然后再个位数相同的放一起，然后在合并，在十位相同的放一起，再合并，直至比到最大位为止，再合并即可)
 */
fun baseSort() {
    val tempList = arrayListOf<Int>(
        78,
        23,
        2,
        444,
        90,
        2,
        89,
        22,
        46,
        20,
        42,
        3,
        89,
        23,
        11,
        57,
        90,
        25,
        888,
        8888,
        9999
    )

    Log.e("Algorithm_baseSort", "---------------->基数原始列表：$tempList")
    var max = tempList.first()
    tempList.forEach {
        max = it.coerceAtLeast(max)
    }
    val value = if (max.toString().contains(".")) {
        max.toString().split(".")[0]
    } else {
        max.toString()
    }
    val counts = value.length
    val indexArray = arrayOfNulls<ArrayList<Int>>(10)
    for (i in 1..counts) {
        tempList.forEach {
            val length = if (it.toString().contains(".")) {
                it.toString().split(".")[0]
            } else {
                it.toString()
            }.length
            val index = if (length <= i) {
                it / (10.0.pow(i - 1))
            } else {
                if (i > 1)
                    it % (10.0.pow(i) / 10.0.pow(i - 1))
                else
                    it % (10.0.pow(i))
            }.toInt()


            if (indexArray[index] == null) {
                indexArray[index] = arrayListOf()
            }
            val arrayList = indexArray[index]
            arrayList?.add(it)
        }
        tempList.clear()
        indexArray.forEachIndexed { index, list ->
            tempList.addAll(list ?: arrayListOf())
            indexArray[index] = null
        }
        Log.e("Algorithm_baseSort", "       ---------------->基数列表：$tempList")
    }
    Log.w("Algorithm_baseSort", "---------------->基数列表：$tempList")
    Log.e("Algorithm_baseSort", "-------------------->基数end<----------------------\n\n")
}