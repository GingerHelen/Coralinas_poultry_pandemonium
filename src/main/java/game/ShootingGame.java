package game;

import java.awt.*;
import java.awt.event.*;
import java.util.ArrayList;
import java.util.Random;
import javax.swing.*;
import javax.swing.border.Border;

public class ShootingGame extends JPanel {


    private int width = 650;  // ширина окна
    private int height = 600; // высота окна
    private int score = 0; // счет
    private Image backgroundImage; // фон для основной части игры
    private Image backgroundStartButton; // фон для старта игры
    private int numOfTargets = 5; // число куриц на экране
    private ArrayList<Target> targets = new ArrayList<>(); // список куриц
    private boolean isStarted = false; // флаг начала игры

    public ShootingGame() {
        backgroundImage = new ImageIcon("C:/Users/helen/Downloads/background3.jpg").getImage();
        backgroundStartButton = new ImageIcon("C:/Users/helen/Downloads/startButton.jpg").getImage();
        for (int i = 0; i < numOfTargets; i++) {
            targets.add(new Target());
        }

        // слушатель нажатий мыши
        addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                super.mouseClicked(e);
                int x = e.getX();
                int y = e.getY();
                // проверяем попадание в курицу
                for (int i = 0; i < targets.size(); i++) {
                    Target target = targets.get(i);
                    if (target.isHit(x, y)) {
                        targets.remove(target);
                        score++;
                    }
                }
            }
        });

        final JLabel label = new JLabel("Для запуска игры нажмите любую кнопку");
        label.setForeground(Color.WHITE);
        Font font = new Font("Verdana", Font.BOLD, 17);
        label.setFont(font); // устанавливаем шрифт
        label.setHorizontalAlignment(JLabel.CENTER);

        // панель с BorderLayout
        JPanel panel = new JPanel(new BorderLayout());
        panel.setPreferredSize(new Dimension(getWidth(), getHeight())); // Задаем размер панели
        label.setVerticalAlignment(JLabel.BOTTOM);
        Border border = BorderFactory.createEmptyBorder(460, 0, 10, 0);
        label.setBorder(border);

        panel.add(label, BorderLayout.SOUTH);

        add(panel); // Добавляем панель на основную панель игры
        add(label);
        addMouseListener(new MouseAdapter() {
                             @Override
                             public void mouseClicked(MouseEvent e) {
                                 isStarted = true;
                                 label.setVisible(false);

                             }
                         });
        addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) {
                isStarted = true;
                label.setVisible(false);

            }
        });

        setFocusable(true);
        requestFocusInWindow();

        // запускаем поток обновления экрана
        Thread updateThread = new Thread(new Runnable() {
            public void run() {
                while (true) {
                    try {
                        Thread.sleep(30); // задержка 30 мс
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    if (isStarted) {
                        // обновляем положение тарелок
                        for (int i = 0; i < targets.size(); i++) {
                            Target target = targets.get(i);
                            target.update();
                            // если тарелка вышла за границы экрана - удаляем ее
                            if (target.isOutside(width, height)) {
                                targets.remove(target);
                            }
                        }
                        // если число куриц меньше заданного - добавляем новые
                        if (targets.size() < numOfTargets) {
                            targets.add(new Target());
                        }
                        repaint(); // перерисовываем окно
                    }
                }
            }
        });
        updateThread.start();
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        if (isStarted) {
            g.drawImage(backgroundImage, 0, 0, getWidth(), getHeight(), null);
            // рисуем куриц
            for (int i = 0; i < targets.size(); i++) {
                Target target = targets.get(i);
                target.paint(g);
            }

            // рисуем счет
            g.setColor(Color.WHITE);
            g.drawString("Score: " + score, 10, 20);
        } else {
            g.drawImage(backgroundStartButton, 0, 0, getWidth(), getHeight(), null);
        }
    }

    public static void main(String[] args) {
        JFrame frame = new JFrame("Shooting Game");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(650, 600);
        frame.setResizable(false);
        frame.add(new ShootingGame());
        frame.setVisible(true);
    }

    private class Target {
        private int x, y; // координаты
        private int size = 50; // размер
        private int speed = 5; // скорость
        private int dx, dy; // направление движения

        public Target() {
            Random r = new Random();
            x = r.nextInt(width - size);
            y = r.nextInt(height - size);
            dx = r.nextInt(speed * 2) - speed;
            dy = r.nextInt(speed * 2) - speed;
        }

        public void update() {
            x += dx;
            y += dy;
        }

        public boolean isHit(int px, int py) {
            return px >= x && px <= x + size && py >= y && py <= y + size;
        }

        public boolean isOutside(int width, int height) {
            return x < 0 || y < 0 || x > width || y > height;
        }

        public void paint(Graphics g) {
            g.setColor(Color.RED);
            g.fillOval(x, y, size, size);
        }
    }
}