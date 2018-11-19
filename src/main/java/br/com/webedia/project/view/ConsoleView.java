package br.com.webedia.project.view;

import java.awt.EventQueue;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JTextArea;
import javax.swing.border.EmptyBorder;

import br.com.webedia.project.connection.Server;

/**
 * <h1>Console</h1> Essa é a classe inicial da aplicação, utiliza-se de herança
 * da classe JFrame para criar a interface para interação.
 * <p>
 * A tela consiste de uma área de texto que serve como log, e é atualizada em
 * vários momentos da aplicação, e também de dois botões que iniciam e fecham o
 * servidor.
 * 
 * @author Victor Correa
 *
 */
public class ConsoleView extends JFrame {

	private static final long serialVersionUID = 1L;
	private JPanel contentPane;
	private JButton btnStart;
	private JButton btnClose;
	private static JTextArea textArea = new JTextArea();

	/**
	 * Método main, que inicia a aplicação.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					ConsoleView frame = new ConsoleView();
					frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * Construtor que cria a interface.
	 */
	public ConsoleView() {

		// Configurações do JPanel
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 605, 395);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(null);

		// Configurações do botão Start Server
		btnStart = new JButton("Start Server");
		btnStart.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {

				// Configuração do evento de clique do botão.
				try {

					// Inicia o servidor.
					Server.startServer();

					// Desabilita o botão Start Server.
					btnStart.setEnabled(false);

					// Habilita o servidro Close Server.
					btnClose.setEnabled(true);

				} catch (Exception e1) {

					ConsoleView.addText("Erro :" + e1.getMessage());

				}

			}
		});
		btnStart.setBounds(10, 306, 120, 39);
		contentPane.add(btnStart);

		// Configurações do botão Close Server
		btnClose = new JButton("Close Server");
		btnClose.setEnabled(false);
		btnClose.addActionListener(new ActionListener() {
			// Configuração do evento de clique do botão.
			public void actionPerformed(ActionEvent e) {

				// Fecha o servidor.
				Server.closeServer();

				// Habilita o botão Start Server.
				btnStart.setEnabled(true);

				// Desabilita o botão Close Server.
				btnClose.setEnabled(false);
			}
		});
		btnClose.setBounds(140, 306, 120, 39);
		contentPane.add(btnClose);

		// Configuração da área de texto
		textArea.setEditable(false);
		textArea.setBounds(10, 11, 569, 289);
		contentPane.add(textArea);
	}

	/**
	 * Método utilizado para atualizar o conteúdo da área de texto da interface.
	 * 
	 * @param conteudo Mensagem a ser gravada na area de texto.
	 */
	public static void addText(String conteudo) {
		textArea.append(conteudo + "\n");
	}
}
