# game 2048

![](https://i.imgur.com/MVgrG3Q.png)

## Description

使用 Java 開發的 2048 小遊戲，花費時間大約三天。

若發現 Bug 🐛️ 歡迎聯繫。

## Requirement

JDK 版本：Adoptium Eclipse Temurin 17

## 素材來源

本人做圖苦手，所有圖片皆使用 [Fakeimg.pl](https://fakeimg.pl/) 產生。

附上我的素材 HTML code：https://codepen.io/vactiger/pen/gOQpzLx。

## 心得

過程中最難的應該是對於**使用陣列實現遊戲邏輯**，如上下左右移動、判斷是否輸掉等。

從中複習到 OO 的封裝、繼承、多型，還有名字很像但概念完全不一樣的 Override（覆寫）／Overload（多載）；interface 與 extends 的差異；還有 public／protected／default／private 的權限區別。

## 待完成的功能

當某個格子達到 2048 後：

1. 遊戲獲勝 + 再來一局
1. 遊戲繼續 + 2048 counter（用來計算你完成幾次 2048）+ 該格子消失