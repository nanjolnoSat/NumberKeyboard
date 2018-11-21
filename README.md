# NumberKeyboard
效果图<br/><img src="https://github.com/nanjolnoSat/NumberKeyboard/blob/master/nkv001.gif"/><img src="https://github.com/nanjolnoSat/NumberKeyboard/blob/master/nkv002.gif"/><br/>
输入框为InputNumberView,数字键盘为NumberKeyboardView<br/>
在需要使用输入框的地方使用InputNumberView,在需要使用键盘的地方使用NumberKeyboardView,然后使用InputNumberView的setNumberKeyboard方法将InputNunmberView和NumberKeyboradView关联起来即可.<br/>
如需改变数字键盘的样式,只需使用NumberKeyboardView的getNkAdapter()拿到NkAdapter后设置callback的属性即可.当返回null的时候,表示使用原来的View,否则使用返回的View.<br/>
