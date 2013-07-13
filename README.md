ConvertorBackslash
==================

### Eclipse插件 ###

#### 使用场景 ####

	Windows下Java开发，经常复制文件（夹）路径.

	由于Windows下路径分隔符是“\”，而在Java语法中“\”是转义符，所以经常需要手动或者使用替换功能将路径中“\”替换为“/”。

	本插件用于将选中文本中的“\”转为“/”。

#### 使用方式 ####
	Eclipse下选中文本，按下Shift+Alt+C快捷键，会自动将选中文本中的“\”替换为“/”。

#### 备注 ####
	插件默认快捷键为Shift+Alt+C，可在Eclipse-Window-Preferences-General-Keys中搜索“Convertor Backslash”找到对应的快捷键绑定，更改快捷键。

#### 版本历史 ####
	1.0.1
		参考Eclipse官方转换大小写的CaseAction源码对插件进行了重构，增强兼容性，减少不必要的类型判断
	1.0.2
		去除右键菜单：由于无法配置右键菜单仅出现在编辑视图中
		其他细节优化及调整