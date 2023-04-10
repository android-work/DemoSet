package com.android.work.demoset

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.util.Log
import android.util.SparseArray
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.core.util.putAll
import androidx.recyclerview.widget.RecyclerView
import com.android.work.bluetooth.BlueActivity
import com.android.work.demoset.databases.room.RoomActivity
import com.android.work.demoset.deep.DeepLinkActivity
import com.android.work.demoset.design.DesignPatternActivity
import com.android.work.demoset.hook.TestHookActivity
import com.android.work.demoset.hot_fix.HotFixTest
import com.android.work.demoset.permission.PermissionActivity
import com.android.work.demoset.plc.PLCDemoActivity
import com.android.work.demoset.provider.ContentProviderTestActivity
import com.android.work.demoset.result.StartActivity1
import com.android.work.demoset.web.WebActivity
import okhttp3.*
import java.util.*
import java.util.concurrent.TimeUnit
import com.android.work.bluetooth.R as r


class MainActivity : AppCompatActivity() {

    /**
     * Handler构造方法：
     *      1、无参、一参callback/async 的构造方法，最终都是调用 二参callback，async；内部处理是获取当前线程looper，并校验是否存在、从looper中获取消息队列
     *      2、带有loop的构造方法，最后都是调用 三个参数 loop，callback，async构造方法，内部逻辑就是赋值操作
     *   a、handler.sendMessage(msg)：通过发送消息
     *      1、内部调用链是执行sendMessageDelay(msg,time),只不过time = 0
     *      2、会将handle对象赋值给msg.target，用于后面取出message发送到handler中处理作准备
     *      3、然后会从looper中获取MessageQueue，并执行mQueue.enqueueMessage()，将msg存入队列中
     *
     *   b、handler.dispatchMessage(msg)：处理消息
     *      1、先判断msg.callback是否存在，存在则优先执行msg.callback.run()
     *      2、否则判断handler.callback是否被实现，存在则执行callback.handleMessage(msg)
     *      3、否则再执行handler.handleMessage(msg)，即我们自己实现的handler逻辑
     *
     *
     * Looper：
     *      1、通过Looper.prepare()静态方法创建Looper对象
     *      2、在prepare方法中，先从ThreadLocal中取出当前线程中的Looper，并校验是否存在Looper，如果存在则抛异常，不存在则new Looper()
     *      3、在Looper构造方法中，会去创建MessageQueue对象
     *   a、Looper.loop():开启循环遍历消息
     *      1、在looper的操作中，优先检查当前线程是否存在looper对象
     *      2、创建无限for循环，迭代消息队列中的msg，在取出msg时，MessageQueue.next()，内部会开启无限for循环，判断拿到的msg等待时常是否结束，是则返回，不是继续for循环
     *      3、如果当取出的msg不存在，则停止循环
     *      4、否则就执行msg.target.dispatchMessage(msg)到handler中
     *
     * ThreadLocal：
     *   a、set(value)：存入
     *      1、先获取到当前线程
     *      2、从当前线程中获取到ThreadLocalMap
     *      3、根据当前的ThreadLocal对象作为key，将value存入到ThreadLocalMap内部的健值对中
     *
     *   b、get()：取出
     *      1、先获取到当前线程
     *      2、从当前线程中获取到ThreadLocalMap
     *      3、根据当前的ThreadLocal对象作为key，从ThreadLocalMap内部中取出对应的value
     *
     *
     * Message:
     *      1、创建Message通过Message.obtain()静态方法创建，目的是为了复用之前的Message对象
     *      2、复用之前的Message，其逻辑就是将已处理过的Message中的状态全重置，然后将新的Message状态赋值给复用的Message中，达到Message对象可重复使用，无需创建过多Message对象
     *   a、enqueueMessage()：存消息
     *      1、在enqueueMessage中，按等待时间进行排列到消息队列中
     *          a、待执行消息：sMessage，目标消息：msg，临时消息变量：p，
     *          b、先取出待执行的消息sMessage
     *              1、如果sMessage不存在/msg.when == 0/msg.when < sMessage.when，都是将目标msg消息置为队列首位，并将sMessage指向msg
     *              2、否则，遍历队列，找到msg.when < p.when，并将msg插入到p的前面
     *
     * 总结：
     *      1、由于在Handler构造方法中回去校验当前线程是否存在Looper对象，所有我们在自线程中new Handler没有创建Looper会抛异常
     *      2、通过创建Looper对象，我们发现在线程中仅存在且唯一的Looper对象
     *      3、handler机制中的延迟消息，不是延迟发送，而是延迟从消息队列中取出，进而延迟处理
     *      4、ThreadLocal的存储机制是线程安全的，因为ThreadLocal是作为key存放在当前线程中的ThreadLocalMap的一个弱引用的一个key-value健值对中，而线程是独立的，其他线程无法访问当前线程
     *
     */
    private val handler = Handler(Looper.myLooper()!!)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        /**
         * activity中调用setContentView，先在AppCompatDelegateImpI.setContentView执行
         *      1、创建根布局，根据设置window窗口属性选择根布局root(一般root布局是LinearLayout->title->content)
         *      2、执行window.setContentView(root)
         *      3、获取DecorView，并将root添加到DecorView中(其中会执行viewRootImpI.requestFitSystemWindows,用于检查线程安全以及使用handler消息刷新页面)
         *      4、获取到root中到content布局，将我们绘制的xml解析出view并添加到content中
         *
         *
         * LayoutInflater加载布局
         *      1、先获取xml解析器
         *      2、通过解析器读取xml布局文件
         *      3、先读取第一个根节点，并获取节点名称，根据名称进行反射创建控件对象(判断name是否包含'.',如果包含，可以作为自定义控件，直接反射全路径名；如果不包含，则视为系统控件，在路径上添加'android.view.'，然后在进行反射控件对象)
         *      4、后面节点通过递归+while循环来读取(目的是将同一深度的控件添加到父布局中，然后递归一层层将子父布局添加到上层父布局中，都是以父布局的布局参数添加)
         *      5、结束xml布局解析后，返回layout，如果inflate传入的root!=null 或 attachParent == true;则会将layout以父布局参数添加到root中；否则直接返回layout
         */
        setContentView(R.layout.activity_main)
    }

        findViewById<RecyclerView>(r.id.rv)

        getH5JumpParams()

        App.v = true

        val okHttpClient = OkHttpClient().newBuilder().readTimeout(1,TimeUnit.MINUTES).writeTimeout(1,TimeUnit.MINUTES).
        addInterceptor(object: Interceptor{
            override fun intercept(chain: Interceptor.Chain): Response {
                val response = chain.proceed(chain.request())
                // 对response进行处理封装
                return response
            }
        }).build()

        /*val request = Request.Builder().addHeader("","").method("", RequestBody.create(MediaType.parse(""),"")).url("").build()
        val call = okHttpClient.newCall(request)
        call?.enqueue(object:Callback{

            override fun onFailure(call: okhttp3.Call, e: IOException) {
                TODO("Not yet implemented")
            }

            override fun onResponse(call: okhttp3.Call, response: Response) {
                TODO("Not yet implemented")
            }

        })*/

        test()

    fun hotFix(view: android.view.View) {
        HotFixTest().hotFix()
    }

    private fun getH5JumpParams() {
        val uri = intent.data
        Log.d("TAG","uri:${uri?.toString()}")
        uri?.apply {
            val a = getQueryParameter("a")
            val b = getQueryParameter("b")
            val c = getQueryParameter("c")
            val url = getQueryParameter("url")
            if (url?.isNotEmpty() == true){
                startActivity(Intent(this@MainActivity,DeepLinkActivity::class.java))
            }
            Log.d("TAG","a:$a   b:$b   c:$c")
        }
    }

    fun contentProvider(view: View){
        startActivity(Intent(this,ContentProviderTestActivity::class.java))
    }

    fun aidlTest(view: View) {
        startActivity(Intent(this, PLCDemoActivity::class.java))
    }

    fun designPatternTest(view: View) {
        startActivity(Intent(this,DesignPatternActivity::class.java))
    }

    fun hookBlackWhite(view: View) {
        val intent = Intent(this, TestHookActivity::class.java)
        intent.putExtra("isHook",false)
        startActivity(intent)
    }

    fun roomTest(view: View) = startActivity(Intent(this,RoomActivity::class.java))

    fun activityResult(view: View) = startActivity(Intent(this,StartActivity1::class.java))

    fun permissionRequest(view: View) = startActivity(Intent(this,PermissionActivity::class.java))

    fun web(view: View) = startActivity(Intent(this,WebActivity::class.java))

    val list = arrayListOf<String>()
    val linkList = LinkedList<String>()
    val sparseArray = SparseArray<String>()
    val hashMap = hashMapOf<String,String>()

    fun test(){
        /**
         * 1、构造方法：
         *      ArrayList(): 初始化elementData,为期赋值默认的的空object数组
         *      ArrayList(size):校验size的合法性，size != 0,创建size大小的object数组，size == 0,赋值空object数组
         *      ArrayList(Collection):将添加集合转数组，将数组赋值给elementData，并给集合大小赋值，然后进行泛型擦出
         *
         * 2、添加元素add()
         *      a、先进判断是否需要扩容：
         *          1、判断是通过无参构造方法创建集合，第一次添加元素会默认扩容为10容量，后面继续添加会将添加元素
         *        后的最小容量与默认容量10取最大值，作为后面是否需要扩容的依据
         *          2、通过其他构造方法创建的直接走b逻辑
         *      b、得到添加元素后的最小容量使他与当前数组容量比较，大于的话，需要进行扩容
         *          扩容规则：将原数组容量扩大到1.5倍，如果还小于添加后的容量，则将添加后容量作为新扩容的容量。最大无法超过Int.maxValue
         *      c、处理完后，目标元素添加到扩容后的数组size位置，并将size++
         *
         * 3、添加元素add(index,element)
         *      a、先校验index合法性，是否越界
         *      b、判断是否需要扩容，逻辑同场景2
         *      c、将原数组中的index位置元素及后面全部元素往后挪一位
         *      d、将index位置的元素赋值为element，size ++
         *
         * 4、添加元素addAll(Collection)
         *      a、将添加集合转数组a
         *      b、校验是否需要扩容，逻辑同上
         *      c、将数组a拷贝到elementData数组上
         *      d、size += a.length
         *
         * 5、移除元素remove(index)
         *      a、校验index合法性，是否越界
         *      b、将index + 1 及后面所有的元素往前挪一位到index位置
         *      c、将size - 1位置的元素置空待GC回收
         *      d、size--
         *
         * 6、移除元素remove(element)
         *      a、判断element == null：
         *          1、for循环elementData数组，找到第一个元素为null的元素，并得到index；
         *          2、方法同场景5的步骤b、c、d
         *      b、element != null：
         *          1、for循环elementData数组，找到第一个元素值与目标元素值相等，并得到index；
         *          2、方法同上步骤2
         *
         *
         * 总结：ArrayList底层数据结构是数组，具有查找快，增删慢的特点，以及扩容也是一个个元素的挪动；ArrayList是有序的；避免频繁的增删操作
         */
        list.add("")
        list.addAll(list)
        list.remove("")

        /**
         * 1、构造方法：
         *      LinkedList():空实现
         *      LinkedList(Collection):内部走的是addAll的逻辑，下面会介绍
         *
         * 2、添加元素add(element)
         *      a、将element包装成Node并指向pre = last节点、next = null，将node节点链到last节点后
         *      b、判断last == null，即判断是否是空链表
         *          是：first指向node
         *          不是：将原最后一个节点last的next节点指向node，将node前一个节点pre指向原最后一个节点last
         *      c、最后将last节点指向node节点
         *
         * 3、添加元素add(index,element)
         *      a、先检查index合法性，是否越界
         *      b、通过二分法查找index，找到目标插入元素的位置,找到index位置节点xNode,构建element的node节点，其pre节点为xNode，next节点为xNode.next
         *      c、将node节点与前后节点链起来，xNode.pre.next节点为node节点，xNode.pre节点为node节点，
         *      d、size++
         *
         * 4、添加元素addAll(index,Collection)
         *      a、检查索引合法性，是否越界
         *      b、将插入的子集合转object数组a
         *      c、通过二分法找到index所在的插入的目标节点xNode
         *      c、for循环将数组a中的元素转Node节点，并将数组a转成链表，
         *      d、将xNode.pre.next节点指向插入子链表的第一个节点cFirstNode，将xNode.pre节点指向插入子链表的最后一个节点cLastNode
         *            如果插入的是队首，需要将first节点赋值cFirstNode节点
         *            如果插入的是队尾，需要将last节点赋值cLastNode节点
         *      e、size += c.size()
         *
         * 5、删除元素remove(element)
         *      a、先遍历容器，找到对应的node节点
         *      b、将node.pre.next指向node.next,将node.next.pre指向node.pre
         *      c、将node.pre = null;node.next = null; node.item = null
         *      d、size --
         *
         * 6、删除元素remove(index)
         *      a、先校验index合法性，是否越界
         *      b、通过二分法查找目标节点xNode
         *      c、操作同场景5
         *
         * 总结：LinkedList底层数据结构是链表，特点是增删快(插入队首/队尾,其他位置需要通过二分法找到相应位置的节点)、查找相比较慢
         */
        linkList.add("1")
        linkList.add("2")
        linkList.add("3")
        linkList.add("4")
        linkList.addAll(linkList)
        linkList.remove("")

        /**
         * 1、构造方法
         *      SparseArray():无参构造方法，内部调用的是有参构造方法，并传入10作为默认大小
         *      SparseArray(Int):有参构造方法，判断传入的容量是否为0
         *          是：给mKeys初始化长度为0的int数组，给mValues初始化长度为0的Object数组
         *          否：给mValues创建一个指定长度的object数组，并给mKeys创建同长度的int数组
         *
         * 2、添加元素put(int,element)
         *      a、先通过二分法查找适合添加key所属的位置i：
         *          i >= 0:表示找到相同的key，直接覆盖mValues所对应的i索引值
         *          i < 0:返回的是key插入mKeys最合适的位置索引的补码
         *              1、对索引的补码进行取补，得到正确的索引值
         *              2、如果插入的位置小于集合大小并且插入的位置mValues恰巧是默认的Object对象(表示被remove的元素没有被内部gc置null)，
         *            这时候直接将对应的mKeys、mValues的值替换
         *              3、如果步骤2不满足，此时如果需要等待内部gc置null之前remove的元素，则执行内部gc方法(具体实现说明在下面remove方法中的gc内部实现)
         *              4、通过步骤3，会将有效的mKeys及mValues进行整理，将被remove的元素移除，并将有效值往前移动，保证数组的连续性
         *              5、然后对整理后的集合重新进行二分法查找key插入的合适位置i(因为此时数组的长度有变化，导致之前查找的位置无效)
         *              6、插入的位置找到，分别对mKeys、mValues进行检测是否需要扩容：将添加元素后的集合大小与对应的数组长度比较，如果大于则需要进行扩容到原来的2倍，反之不需要
         *                  a、扩容：新创建一个与原数组同类型且容量是之前的2倍的数组，将原先的数组从0 ～ i的位置元素拷贝到新数组中，再将i位置的元素改成目标
         *                值，最后将i ～ 剩的元素值拷贝到新数组中的i + 1的位置，并将新数组返回
         *                  b、不扩容：将i ～ 剩下有效的位置元素往后移一位，将目标元素放到i位置，返回原数组
         *              7、size ++
         *
         * 3、内部gc()
         *      a、执行for循环，循环次数是mKeys的大小
         *      b、循环体是对mValues的遍历，查询是否存在value = 默认的Object对象，存在就跳过此次循环
         *      c、不存在，就判断i 与 o 是否相等：相等则进行 o++，并执行下次循环
         *      d、不相等，就将mKeys、mValues对应i位置的元素赋值到o对应的位置，然后将mValues的i位置元素置null，等待系统gc清理
         *      e、结束循环后，将o赋值为size大小
         *
         * 4、添加元素putAll(SparseArray)
         *      将插入的列表进行遍历，然后获取对应的key、value再执行put方法
         *
         * 5、删除元素remove(key)
         *      a、通过二分法找到key所在的位置
         *      b、将mValues对应位置的元素设置为默认的Object对象(用于执行内部gc逻辑的判断)
         *      c、将需要执行gc的标识置为true(用于在操作集合数据时，是否需要做gc的判断)
         *
         *
         * 总结：SparseArray底层数据结构是数组，通过key所在的索引找到value，避免了hashMap内部的数组+链表+红黑树的重逻辑，因为使用数组，查找会较快
         * 避免频繁删除操作(频繁的删除可能会导致大量的Object对象无法被回收)
         */
        sparseArray.put(1,"")
        sparseArray.append(1,"")
        sparseArray.putAll(sparseArray)
        sparseArray.remove(1)

        /**
         * 1、构造方法：
         *      HashMap()：内部设置默认扩容系数，0.75
         *      HashMap(int)：内部调用两个参数构造方法，设置容量、设置默认扩容系数0.75
         *      HashMap(int,float)：内部先校验容量是否合法，再判断容量是否达到2^30，如果达到则设置容量为2^30，设置传入的扩容系数，计算扩容条件
         *      HashMap(Map)：
         *
         * 2、添加元素put()
         *      a、根据传入的key，计算出具有离散属性的hash值(将key进行hash ^ hash >> 16)
         *      b、添加元素：
         *          1、第一次添加元素(数组为null)：需要创建数组
         *              a、通过无参构造方法创建对象：创建一个大小为16的数组，计算出扩容参数(size * 0.75)
         *              b、通过有参构造方法创建对象：按传入的容量大小进行创建数组，计算扩容参数(同上)
         *           -> 得到新数组后，通过hash & (size - 1) 得到key所在新数组中的索引，然后将Node保存到对应位置
         *          2、存在数组：
         *              a、是树节点，将节点按红黑树的规则，添加到树中
         *              b、是链表节点，遍历链表，是否存在相同key，存在，则将对应节点替换，不存在就添加到链表最后一个节点上，判断链表的长度是否超过6，
         *            超过再判断数组长度>64，此时需要将链表转为树，否则对数组进行扩容
         *
         * 3、扩容resize()
         *      a、创建一个2倍容量的新数组
         *      b、循环遍历老数组
         *          1、当前节点不是链表、树：通过计算hash & (newSize - 1)得到在新数组中的位置，并设置元素
         *          2、当前节点是树：先将树拆成2个链表，并判断链表长度是否超过6，对超长的链表转为树后，都插入到数组中
         *          3、当前节点是链表：将链表拆分成2个链表(二分法拆成高低链)，然后分别插入到数组中(低链的位置为原先在老数组中的位置j，高链的位置为 j + oldSize)
         *      c、返回新数组
         *
         * 总结：
         *      1、非通过传入map的构造方法创建的对象，均只是创建hashMap对象，无内部数组初始化(在put元素时，才对数组进行初始化)
         *      2、hashMap的默认长度为16、链表的长度 <= 6
         *      3、扩容规则：链表长度 <= 6 && 数组长度 < 64 才会对数据进行扩容(扩到原来2倍)，否则 链表长度 > 6,会将链表转为红黑树
         *      4、hashMap是无顺序的：
         *          a、数组扩容：
         *              1、在数组扩容时，会对树表进行拆分为2个链表，如果链表长度过长，重新对超长链表转成红黑树，插入的数组中
         *              2、在数组扩容时，会对链表拆分成2个子链，重新插入数组中
         *          b、在添加元素时，如果插入到红黑树时，按红黑树的规则，需要保证树平衡，会导致树的旋转，导致祖、父、兄节点位置的变化
         *      5、hashMap的容量最大为2^30
         */
        hashMap.put("","")
        hashMap.putAll(hashMap)
        hashMap.remove("")
    }


    /**
     * startActivity()
     * 1、调用startActivity，通过一系列调用链，最终调用Activity.startForResultActivity()
     * 2、在startForResultActivity中，会调用Instrumentation.execStartActivity()，并携带一个IApplicationThread的IBind对象
     * 3、在execStartActivity中，获取到IActivityTaskManager的ActivityTaskManagerService代理对象(供应用进程调用服务进程)，并执行
     *   ActivityTaskManagerService.startActivity()，并将IApplicationThread的IBind对象传入(为服务进程调用应用进程)
     * 4、在startActivity中，最终会执行到ActivityStarter中的execute()，在这里面主要进行开启activity的请求，解析信息并将一系列信息封装到ActivityRecord中
     * 5、然后创建一个ClientTransaction对象，保存了应用进程IApplicationThread的IBind对象(用于服务进程与应用进程的通信)，以及IApplicationToken也是IBind对象，内部保存了ActivityRecord对象
     * 6、最后执行ClientTransaction.schedule()，内部调用IApplicationThread.scheduleTransaction(ClientTransaction)，夸进程通信
     * 7、在ApplicationThread.scheduleTransaction()中，会执行ActivityThread.scheduleTransaction()
     * 8、在ActivityThread中会执行父类的scheduleTransaction()，最终发送一个EXECUTE_TRANSACTION消息到ActivityThread.H的Handler内部类中
     * 9、在EXECUTE_TRANSACTION消息处理中，最后是根据Lifecycle的状态处理activity的生命周期
     * 10、ON_CREATE状态：
     *      a、执行ActivityThread.handleLaunchActivity()
     *      b、然后通过执行Instrumentation.newActivity()，内部通过反射创建一个activity对象
     *      c、然后从判断是否application对象，没有就执行Instrumentation.newApplication()，同样是通过反射创建Application
     *      d、application对象创建完毕后，指向application.attach()，在attach中，会将application的context对象保存到ContextWapper中
     *      e、attact执行完后，通过Instrumentation对象最终执行application.onCreate()
     *      f、application创建完毕后，接着执行activity的attach()，在attach中创建PhoneWindow，并保存activity的context到ContextWapper中
     *      g、之后在执行onCreate方法
     * 11、同理等Lifecycle的状态变化，执行对应状态的方法，最终回调activity的生命周期
     */
    fun blueTest(view: View) {
        startActivity(Intent(this,BlueActivity::class.java))
    }

    fun hotFix(view: View) {
        HotFixTest().hotFix()
    }


}