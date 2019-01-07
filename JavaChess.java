/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package javachess;

import java.awt.Color;
import java.awt.Container;
import java.awt.Graphics;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.util.ArrayList;
import javax.swing.JFrame;
import javax.swing.JOptionPane;

/**
 *
 * @author lab304
 */
class chessFrame extends JFrame implements MouseListener{
    private static final int sx = 75;//小方格宽度
    private static final int sy = 75;//小方格高度
    private static final int w = 50;
    private static final int rw = 350;
    private static int[][] chess ;
    
    
    private Graphics jg;
    
    
    

    
    /**
     * DrawSee构造方法
     */
    public chessFrame() {
        this.addMouseListener(this);
        Container p = getContentPane();
        setBounds(100, 100, 500, 500);
        setVisible(true);
        p.setBackground(Color.YELLOW);
        setLayout(null);   
        setResizable(false);
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);        
        chess = new int[8][8];
        try {
            Thread.sleep(500);
        } catch (Exception e) {
            e.printStackTrace();
        }        
        for(int i = 0;i<8;i++)
            for(int j = 0;j<8;j++)
                chess[i][j] = 0;
        // 获取专门用于在窗口界面上绘图的对象
        jg =  this.getGraphics();
        
        // 绘制游戏区域
        paintComponents(jg);
        
        
    }
    
    
    
    public void paintComponents(Graphics g) {
        try {
            
            // 设置线条颜色为红色
            g.setColor(Color.RED);
            // 绘制外层矩形框
            g.drawRect(sx, sy, rw, rw);
            for(int i = 1; i < 8; i ++) {
                // 绘制第i条竖直线
                g.drawLine(sx + (i * w), sy, sx + (i * w), sy + rw);
                // 绘制第i条水平线
                g.drawLine(sx, sy + (i * w), sx + rw, sy + (i * w));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    public void AI() {
        int[][] a = new int[8][8];
	int res = -1;
        ArrayList open = new ArrayList();
        for (int i = 0; i < 7; i++) {
            for (int j = 0; j < 7; j++) {
                if (chess[i][j] == 0) {
                    open.add(i * 8 + j);
                }
                a[i][j] = chess[i][j];
            }
        }
        int findone = Integer.MIN_VALUE;

        for (int index = 0;index < open.size();index++)
	{
            int one =  (int)open.get(index);
            a[one / 8][one % 8] = -1;
            int findtwo = Integer.MAX_VALUE;
            int alfa = Integer.MIN_VALUE;
            for (int index1 = 0;index1 < open.size();index1++ )
            {
			
		int two = (int)open.get(index1);
                if (two != one) {
                    a[two / 8][two % 8] = 1;
                    int findthree = Integer.MIN_VALUE;
                    for (int index2 = 0;index2 < open.size();index2++)
                    {
			int three =  (int)open.get(index2);
                        if (three != two && three != one) {
                            a[three / 8][three % 8] = -1;
                            int findfour = Integer.MAX_VALUE;
                            for (int index3 = 0;index3 <open.size();index3++)
                            {
							
				int four =  (int)open.get(index3);
                                if (four != three && four != two && four != one) {
                                    a[four / 8][four % 8] = 1;
                                    int numfour = f(a);
                                    if (numfour < findfour) {
                                        findfour = numfour;
                                    }
                                    a[four / 8][four % 8] = 0;
                                    if (numfour < findthree&&numfour < findone) {
                                        break;
                                    }

                                }
                            }
                            if (findfour > findthree) {
                                findthree = findfour;
                            }
                            a[three / 8][three % 8] = 0;
                            if (findthree > findtwo) {
                                break;
                            }
                        }
                    }
                    if (findthree < findtwo) {
                        findtwo = findthree;
                    }
                    a[two / 8][two % 8] = 0;
                    if (findtwo < findone) {
                        break;
                    }

                }
            }
            if (findtwo > findone) {
                findone = findtwo;
                res = one;
            }
            a[one / 8][one % 8] = 0;

        }
        chess[res / 8][res % 8] = -1;
        jg.setColor(Color.WHITE);
        jg.fillOval((res / 8+1)*50, (res % 8+1)*50, 50, 50);
        switch(result()) {
            case 0 :JOptionPane.showMessageDialog(this, "平局！", "消息提示", JOptionPane.INFORMATION_MESSAGE);break;
            case Integer.MAX_VALUE:JOptionPane.showMessageDialog(this, "你输了！", "消息提示", JOptionPane.INFORMATION_MESSAGE);break;
            case Integer.MIN_VALUE:JOptionPane.showMessageDialog(this, "你赢了！", "消息提示", JOptionPane.INFORMATION_MESSAGE);break;
        }
    }
    int f(int[][] ch)
    {
            int res = 0;
            for (int i = 0; i < 8; i++)
            {
                    for (int j = 0; j < 4; j++)
                    {
                            int a = ch[i][j] + ch[i][j + 1] + ch[i][j + 2] + ch[i][j + 3] + ch[i][j + 4];
                            int b = ch[j][i] + ch[j + 1][i] + ch[j + 2][i] + ch[j + 3][i] + ch[j + 4][i];
                            if (a == -5|| b == -5)
                                    return Integer.MAX_VALUE;
                            else if (a == 5|| b == 5)
                                    return Integer.MIN_VALUE;
                            else
                                    res = res + a + b;

                    }
            }
            for (int j = 4; j < 8; j++)
            {
                    for (int i = 0; i < 4; i++)
                    {
                            int a = ch[i][j] + ch[i + 1][j - 1] + ch[i + 2][j - 2] + ch[i + 3][j - 3] + ch[i + 4][j - 4];
                            int b = ch[i][7 - j] + ch[i + 1][8 - j] + ch[i + 2][9 - j] + ch[i + 3][10 - j] + ch[i + 4][11 - j];
                            if (a == -5 || b == -5)
                                    return Integer.MAX_VALUE;
                            else if (a == 5 || b == 5)
                                    return Integer.MIN_VALUE;
                            else
                                    res = res + a + b;
                    }
            }
            return res*-1;
    }
    int result()
    {
            int res = 0;
            for (int i = 0; i < 8; i++)
            {
                    for (int j = 0; j < 4; j++)
                    {
                            if(chess[i][j] >=0 && chess[i][j + 1]  >=0&&chess[i][j + 2] >=0 && chess[i][j + 3] >=0 && chess[i][j + 4]>=0)
                                res++;
                            if(chess[j][i] >=0 && chess[j + 1][i] >=0 && chess[j + 2][i] >=0 && chess[j + 3][i] >=0 && chess[j + 4][i]>=0 )
                                res++;
                            int a = chess[i][j] + chess[i][j + 1] + chess[i][j + 2] + chess[i][j + 3] + chess[i][j + 4];
                            int b = chess[j][i] + chess[j + 1][i] + chess[j + 2][i] + chess[j + 3][i] + chess[j + 4][i];
                            if (a == -5 || b == -5)
                                    return Integer.MAX_VALUE;
                            else if (a == 5 || b == 5)
                                    return Integer.MIN_VALUE;
                    }
            }
            for (int j = 4; j < 8; j++)
            {
                    for (int i = 0; i < 4; i++)
                    {
                           if(chess[i][j] >=0 && chess[i + 1][j - 1] >=0 && chess[i + 2][j - 2] >=0 && chess[i + 3][j - 3] >=0 && chess[i + 4][j - 4]>=0 )
                               res++;
                            if(chess[i][7 - j] >=0 && chess[i + 1][8 - j] >=0 && chess[i + 2][9 - j] >=0 && chess[i + 3][10 - j] >=0 && chess[i + 4][11 - j]>=0 )
                                res++;
                            int a = chess[i][j] + chess[i + 1][j - 1] + chess[i + 2][j - 2] + chess[i + 3][j - 3] + chess[i + 4][j - 4];
                            int b = chess[i][7 - j] + chess[i + 1][8 - j] + chess[i + 2][9 - j] + chess[i + 3][10 - j] + chess[i + 4][11 - j];
                            if (a == -5 || b == -5)
                                    return Integer.MAX_VALUE;
                            else if (a == 5 || b == 5)
                                    return Integer.MIN_VALUE;
                    }
            }
            return res;
    }
    @Override
    public void mouseClicked(MouseEvent e) {
        int x = (e.getX()/50);
        int y = (e.getY()/50);
        chess[x-1][y-1] = 1;
        jg.setColor(Color.BLACK);
        jg.fillOval(x*50, y*50, 50, 50);
        switch(result()) {
            case 0 :JOptionPane.showMessageDialog(this, "平局！", "消息提示", JOptionPane.INFORMATION_MESSAGE);break;
            case Integer.MAX_VALUE:JOptionPane.showMessageDialog(this, "你输了！", "消息提示", JOptionPane.INFORMATION_MESSAGE);break;
            case Integer.MIN_VALUE:JOptionPane.showMessageDialog(this, "你赢了！", "消息提示", JOptionPane.INFORMATION_MESSAGE);break;
        }
        AI();
         //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void mousePressed(MouseEvent e) {
         //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void mouseReleased(MouseEvent e) {
         //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void mouseEntered(MouseEvent e) {
         //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void mouseExited(MouseEvent e) {
         //To change body of generated methods, choose Tools | Templates.
    }
}





public class JavaChess {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        chessFrame chess =new chessFrame();
        // TODO code application logic here
    }
    
}
