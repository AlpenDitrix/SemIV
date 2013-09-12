import java.awt.BorderLayout;
import java.awt.GridLayout;
import java.awt.Rectangle;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;
import javax.swing.UIManager;
import javax.swing.border.EmptyBorder;

public class MainFrame extends JFrame {

	private JPanel contentPane;

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		
		// подключаем стандартный UI системы
		try {
			UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
		} catch (Exception e1) {
			e1.printStackTrace();
		}
		
		// запускаем окно в новом UI-потоке
		SwingUtilities.invokeLater(new Runnable() {
			public void run() {
				try {
					MainFrame frame = new MainFrame();
					frame.setDefaultCloseOperation(EXIT_ON_CLOSE);
					frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * Create the frame.
	 */
	public MainFrame() {
		
		setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		setBounds(100, 100, 450, 300);
		setLayout(new GridLayout(3, 2));
		JButton bGo = new JButton("Roll!") {
			{
				addActionListener(new ActionListener() {

					@Override
					public void actionPerformed(ActionEvent e) {
						// roll();
					}
				});
			}
		};
		JButton bStop = new JButton("Stop!") {
			{
				addActionListener(new ActionListener() {

					@Override
					public void actionPerformed(ActionEvent e) {
						// stop();
					}
				});
			}
		};
		add(bGo);
		add(bStop);
	}
}
