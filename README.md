GridPictureSelector
============
![演示图](image/20190907_114937.gif)<br>
使用开源库 [PictureSelector v2.24](https://github.com/LuckSiege/PictureSelector) 封装个自用九宫格选择器,如有写的不好请多包涵!<br>
注意:本库会带 PictureSelector v2.24 包,如你的项目有,去掉只使用本库引入即可<br>
[![](https://jitpack.io/v/xbr1dge/GridPictureSelector.svg)](https://jitpack.io/#xbr1dge/GridPictureSelector)
### 集成方式
方式一 compile引入
```
dependencies {
	implementation 'com.github.xbr1dge:GridPictureSelector:${lastVersion}'
}
```
项目根目录build.gradle加入
```
allprojects {
   repositories {
      jcenter()
      maven { url 'https://jitpack.io' }
   }
}
```
方式二 maven引入

step 1.
```
<repositories>
	<repository>
	<id>jitpack.io</id>
	<url>https://jitpack.io</url>
	</repository>
 </repositories>
```
step 2.
```
	<dependency>
	    <groupId>com.github.xbr1dge</groupId>
	    <artifactId>GridPictureSelector</artifactId>
	    <version>${lastVersion}</version>
	</dependency>
```
### 基本使用
Activity使用
```
private GridPictureSelectorRecyclerView mRecyclerView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mRecyclerView = findViewById(R.id.rv_main);
        initWidget();
    }

    private void initWidget() {
//        String path1 = "http://img.mp.itc.cn/upload/20160808/83856467c1954dd7b38f9afac56e5cce_th.jpg";
//        String path2 = "https://ss0.bdstatic.com/94oJfD_bAAcT8t7mm9GUKT-xh_/timg?image&quality=100&size=b4000_4000&sec=1567676470&di=afb2bf5e3b617dbc87ca15a25152e84b&src=http://pic1.cxtuku.com/00/15/61/b55283cc310f.jpg";
//        String path3 = "http://clips.vorwaerts-gmbh.de/big_buck_bunny.mp4";
//        String path4 = "/storage/emulated/0/Download/49f2bd68c3f7220e63acdee488c7c75a.mp4";
//        String path5 = "/storage/emulated/0/PictureSelector/CameraImage/PictureSelector_20190830_090424.JPEG";
//
//        mRecyclerView.addDataAll(LocalMediaUtil.create(path1,path2,path3,path4,path5));
		
        //动态设置拖动的开启与关闭
        mRecyclerView.setIsCanDragItem(false);
    }


    /**
     * 回调操作 详情查看PictureSelector库
     */
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        List<LocalMedia> localMedia;
        if (resultCode == RESULT_OK) {
            if (requestCode == PictureConfig.CHOOSE_REQUEST) {// 图片选择结果回调
                localMedia = PictureSelector.obtainMultipleResult(data);

                mRecyclerView.addDataAll(localMedia);
            }
        }
    }
```
.xml引入控件
```
<cc.xbridge.gridpictureselector_library.base.GridPictureSelectorRecyclerView
    android:layout_width="match_parent"
    android:layout_height="wrap_content"
    android:id="@+id/rv_main"/>
```
控件自定义属性
```
<!--列数;默认值为 3-->
<attr name="span_count" format="integer"/>
<!-- 最大选择数;默认值为 9 -->
<attr name="max_item_count" format="integer"/>
<!-- Item 间的水平和垂直间距;默认值为 4dp -->
<attr name="item_white_spacing" format="dimension|reference"/>
<!-- Item 宽度;默认自适应RecyclerView宽度 -->
<attr name="item_width" format="dimension|reference"/>
<!-- 添加按钮图片 -->
<attr name="add_image" format="reference"/>
<!-- 删除按钮图片 -->
<attr name="delete_image" format="reference"/>
```
### 自定义事件(可选,写了会覆盖默认方法)
添加事件
```
mRecyclerView.setOnAddPicClickListener(new OnAddPicClickListener() {
    @Override
    public void onAddPicClick() {
        //TODO 自定义按钮添加事件
    }
});
```
删除事件
```
mRecyclerView.setOnDeletePicClickListener(new OnDeletePicClickListener() {
    @Override
    public void onDeletePicClick(int pos) {
        //TODO 自定义删除按钮事件
    }
});
```
拖动事件
```
mRecyclerView.setOnItemDragListener(new OnItemDragListener() {
            @Override
            public void onItemDragStart(RecyclerView.ViewHolder viewHolder, int pos) {
                //TODO 开始拖动事件
            }

            @Override
            public void onItemDragMoving(RecyclerView.ViewHolder source, int from, RecyclerView.ViewHolder target, int to) {
                //TODO 交换位置事件
            }

            @Override
            public void onItemDragEnd(RecyclerView.ViewHolder viewHolder, int pos) {
                //TODO 松开拖动事件
            }
        });
```
### 自定义样式
选择器样式请查看开源库 [PictureSelector v2.24](https://github.com/LuckSiege/PictureSelector) 文档 