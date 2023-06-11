import javax.swing.*;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.Random;

public class MainFrame extends JFrame implements KeyListener {

    // int[][] data = {
    //         {0, 2, 2, 8},
    //         {32, 4, 0, 4},
    //         {0, 32, 32, 2},
    //         {4, 0, 4, 8},
    // };

    // 測試：遊戲失敗
    // int[][] data = {
    //         {2, 4, 8, 4},
    //         {16, 32, 64, 8},
    //         {128, 2, 256, 2},
    //         {512, 8, 1024, 2048}
    // };

    int[][] data = new int[4][4];


    // 遊戲失敗開關
    boolean loseFlag = false;

    // 遊戲分數
    int score = 0;

    public void initData() {
        generatorNum();
        generatorNum();
    }


    MainFrame() {
        initFrame();
        initData();
        createGameUI();
        // 視窗預設不可見，所以要將其改為可見
        setVisible(true);
    }

    // 初始化視窗
    public void initFrame() {
        setSize(550, 560);
        // 視窗水平垂直置中
        setLocationRelativeTo(null);
        // 設定視窗標題
        setTitle("2048");
        // 取消預設 Layout
        setLayout(null);
        // 關閉視窗時結束程式
        // 等同於 setDefaultCloseOperation(3);
        setDefaultCloseOperation(EXIT_ON_CLOSE);

        this.addKeyListener(this);
    }

    // 建立遊戲畫面
    public void createGameUI() {

        // 清空所有元素
        getContentPane().removeAll();

        // 新增 lose 畫面
        if (loseFlag == true) {
            JLabel loseLabel = new JLabel(new ImageIcon("./2048_assets/lose.png"));
            loseLabel.setBounds(70, 60, 400, 400);
            getContentPane().add(loseLabel);
        }

        // 新增 2048 陣列
        for (int i = 0; i < 4; i++) {
            for (int j = 0; j < 4; j++) {
                JLabel image_2048 = new JLabel(new ImageIcon("./2048_assets/" + data[i][j] + ".png"));
                image_2048.setBounds(55 + 110 * j, 45 + 110 * i, 100, 100);
                getContentPane().add(image_2048);
            }
        }
        // 新增背景
        JLabel background = new JLabel(new ImageIcon("./2048_assets/background.png"));
        background.setBounds(45, 35, 450, 450);
        getContentPane().add(background);

        JLabel scoreLabel = new JLabel("得分: " + score);
        scoreLabel.setBounds(45, 5, 100, 30);
        getContentPane().add(scoreLabel);

        // 畫面重繪
        getContentPane().repaint();
    }

    // 上：38
    // 下：40
    // 左：37
    // 右：39
    @Override
    public void keyPressed(KeyEvent e) {
        int keyCode = e.getKeyCode();

        switch (keyCode) {
            // 左
            case 37 -> {
                moveToLeft(false);
                generatorNum();
            }
            // 上
            case 38 -> {
                moveToTop(false);
                generatorNum();
            }
            // 右
            case 39 -> {
                moveToRight(false);
                generatorNum();
            }
            // 下
            case 40 -> {
                moveToBottom(false);
                generatorNum();
            }
            default -> {
            }
        }

        // 檢查遊戲是否結束（是否無法繼續遊玩）
        checkMove();
        // 移動之後須要重繪畫面
        createGameUI();
    }

    // 左移動
    public void moveToLeft(boolean testFlag) {
        // 將 [非0數字] 直至陣列左方， 將 0 移至陣列右方
        for (int i = 0; i < data.length; i++) {
            int[] tempArray = new int[4];
            int index = 0;
            for (int j = 0; j < data[i].length; j++) {
                if (data[i][j] != 0) {
                    tempArray[index] = data[i][j];
                    index++;
                }
            }
            // 合併相同的數字 與 後方數字前移，並在後面補零
            data[i] = tempArray;

            for (int j = 0; j < 3; j++) {
                if (data[i][j] == data[i][j + 1]) {
                    // 合併相同數字（也就是 x 2）
                    data[i][j] *= 2;

                    // 分數累積
                    // 模擬移動不允許觸發加分，只有在真實移動的情況下才允許加分
                    if (testFlag == false) {
                        score += data[i][j];
                    }

                    // 後方數字前移
                    for (int k = j + 1; k < 3; k++) {
                        data[i][k] = data[i][k + 1];
                    }
                    data[i][3] = 0;
                }
            }
        }
    }

    // 右移動
    public void moveToRight(boolean testFlag) {
        horizontalReverse();
        moveToLeft(testFlag);
        horizontalReverse();
    }

    // 上移動
    public void moveToTop(boolean testFlag) {
        anticlockwise();
        moveToLeft(testFlag);
        clockwise();
    }

    // 下移動
    public void moveToBottom(boolean testFlag) {
        clockwise();
        moveToLeft(testFlag);
        anticlockwise();
    }

    // 測試可否左移動
    public boolean checkLeft() {
        int[][] oldArray = new int[4][4];
        copyArray(data, oldArray);
        moveToLeft(true);

        // 移動後與移動前（oldArray）做對比，如果兩者相等則表示不可移動，不相等則表示可移動

        // true:可以移動
        // false:不可移動
        boolean flag = false;

        // 逐個檢查陣列元素
        loop:
        for (int i = 0; i < data.length; i++) {
            for (int j = 0; j < data[i].length; j++) {
                // 只要有一個陣列元素不相同，就表示可以移動
                if (data[i][j] != oldArray[i][j]) {
                    flag = true;
                    break loop;
                }
            }
        }

        // 檢查完畢後將移動前陣列（oldArray）還原
        copyArray(oldArray, data);

        return flag;
    }

    // 測試可否右移動
    public boolean checkRight() {
        int[][] oldArray = new int[4][4];
        copyArray(data, oldArray);
        moveToRight(true);
        boolean flag = false;
        loop:
        for (int i = 0; i < data.length; i++) {
            for (int j = 0; j < data[i].length; j++) {
                if (data[i][j] != oldArray[i][j]) {
                    flag = true;
                    break loop;
                }
            }
        }
        copyArray(oldArray, data);
        return flag;
    }

    // 測試可否上移動
    public boolean checkTop() {
        int[][] oldArray = new int[4][4];
        copyArray(data, oldArray);
        moveToTop(true);
        boolean flag = false;
        loop:
        for (int i = 0; i < data.length; i++) {
            for (int j = 0; j < data[i].length; j++) {
                if (data[i][j] != oldArray[i][j]) {
                    flag = true;
                    break loop;
                }
            }
        }
        copyArray(oldArray, data);
        return flag;
    }

    // 測試可否下移動
    public boolean checkBottom() {
        int[][] oldArray = new int[4][4];
        copyArray(data, oldArray);
        moveToBottom(true);
        boolean flag = false;
        loop:
        for (int i = 0; i < data.length; i++) {
            for (int j = 0; j < data[i].length; j++) {
                if (data[i][j] != oldArray[i][j]) {
                    flag = true;
                    break loop;
                }
            }
        }
        copyArray(oldArray, data);
        return flag;
    }

    public void checkMove() {
        if (checkLeft() == false && checkRight() == false && checkTop() == false && checkBottom() == false) {
            loseFlag = true;
            System.out.println("遊戲結束");
        }
    }

    // 一維陣列反轉
    public void reverseArray(int[] array) {
        for (int start = 0, end = array.length - 1; start < end; start++, end--) {
            int temp = array[start];
            array[start] = array[end];
            array[end] = temp;
        }
    }

    // 二維陣列水平反轉
    public void horizontalReverse() {
        for (int i = 0; i < data.length; i++) {
            reverseArray(data[i]);
        }
    }

    // 二維陣列順時針旋轉
    public void clockwise() {
        int[][] array = new int[4][4];
        for (int i = 0; i < 4; i++) {
            for (int j = 0; j < 4; j++) {
                array[j][3 - i] = data[i][j];
            }
        }
        data = array;
    }

    // 二維陣列逆時針旋轉
    public void anticlockwise() {
        int[][] array = new int[4][4];
        for (int i = 0; i < 4; i++) {
            for (int j = 0; j < 4; j++) {
                array[3 - j][i] = data[i][j];
            }
        }
        data = array;
    }

    // 二維陣列複製
    public void copyArray(int[][] source, int[][] target) {
        for (int i = 0; i < source.length; i++) {
            for (int j = 0; j < source.length; j++) {
                target[i][j] = source[i][j];
            }
        }
    }

    // 無法監聽方向鍵，不重寫（Skip）
    @Override
    public void keyTyped(KeyEvent e) {

    }

    // 這個 project 用不到，不重寫（Skip）
    @Override
    public void keyReleased(KeyEvent e) {
    }

    public void generatorNum() {
        // 1.建立兩個陣列，記錄空白格子 i 和 j 的 index
        int[] arrayI = {-1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1};
        int[] arrayJ = {-1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1};

        int count = 0;
        // 遍歷每個二維陣列，檢查是否有空格（也就是 0）
        for (int i = 0; i < data.length; i++) {
            for (int j = 0; j < data[i].length; j++) {
                if (data[i][j] == 0) {
                    arrayI[count] = i;
                    arrayJ[count] = j;
                    count++;
                }
            }
        }
        // 如果有找到空格，隨機擇一產生數字 2
        if (count != 0) {
            Random random = new Random();
            int index = random.nextInt(count);
            int x = arrayI[index];
            int y = arrayJ[index];
            data[x][y] = 2;
        }
    }
}
