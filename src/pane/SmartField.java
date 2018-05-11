package pane;

import java.awt.Color;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

import javax.swing.JTextField;
import javax.swing.event.CaretEvent;
import javax.swing.event.CaretListener;
import javax.swing.plaf.ComponentUI;
import javax.swing.plaf.basic.BasicTextFieldUI;

/*
 * Essa Classe � utilizada para ter um controle maior sobre o que o usu�rio informa na janela de di�logo
 */

public class SmartField extends JTextField {
	
	private String caracteresPermitidos, placeholder, tipo;
	private double min, max;
	
	//Construtor simples
	public SmartField() {
		
		this("", "");
	}
	
	//Construtor com caracteres permitidos personalizados
	public SmartField(String caracteresPermitidos) {
		
		this("", caracteresPermitidos);
	}
	
	//Construtor com placeholder e caracteres permitidos personalizados
	public SmartField(String placeholder, String caracteresPermitidos) {
			
		//Construtor do JTextField
		super();
			
		//Definindo placeholder
		this.placeholder = placeholder;
			
		//Definindo conjunto de caracteres permitidos
		this.caracteresPermitidos = caracteresPermitidos;
		
		//Definindo tipo
		this.tipo = "STRING";
	}
	
	//Construtor simples para inputs num�rios
	public SmartField(boolean decimal) {
		
		this(decimal, decimal ? Pane.DOUBLE_MIN : Pane.INT_MIN, decimal ? Pane.DOUBLE_MAX : Pane.INT_MAX);
	}
	
	//Construtor para inputs num�ricos com limites
	public SmartField(boolean decimal, double min, double max) {
		
		//Construtor do JTextField
		super();
			
		//Definindo conjunto de caracteres permitidos e tipo
		if (decimal) {
			this.caracteresPermitidos = "1234567890-.";
			this.tipo = "DOUBLE";
		} else {
			this.caracteresPermitidos = "1234567890-";
			this.tipo = "INT";
		}
		
		//Definindo m�nimo e m�ximo
		this.min = min;
		this.max = max;
	}

	//Sobrescrevendo m�todo que trata os eventos acionados por teclas
	@Override
	protected void processKeyEvent(KeyEvent e) {
		
		//Definindo qual tipo de evento foi acionado
		switch (e.getID()) {
			
			case KeyEvent.KEY_TYPED: {	
				
				//Obtendo lista de key listeners
				KeyListener[] listeners = getKeyListeners();
				
				//Passando evento para os key listeners
				for (KeyListener listener : listeners) {
					listener.keyTyped(e);
				}
				
				//Verificando qual tecla foi digitada
				if (e.getKeyChar() == KeyEvent.VK_BACK_SPACE) {
					
					//Limpa a �ltima casa do texto caso necess�rio
					if (getText().length() > 0) {
						setText(getText().substring(0, getText().length() - 1));
					}
					
				//Adiciona o caractere ao TextField caso esteja na lista de caracteres permitidos
				//Caso a lista esteja vazia, o m�todo entende que podem ser colocadas quaisquer caracteres
				} else if (caracteresPermitidos.contains(String.valueOf(e.getKeyChar())) || (caracteresPermitidos.isEmpty())) {
				
					//Verificando se o tipo do TextField � num�rico
					if (!tipo.equals("STRING")) {
						
						//Verificando se o campo est� preenchido com um 0
						if (getText().equals("0")) {
							
							//Limpa o campo
							setText("");
						}
						
						//Verificando se o caractere � um n�mero
						if ("0123456789".contains(String.valueOf(e.getKeyChar()))) {
							setText(getText() + e.getKeyChar());
						} else {
							
							//Verificando se o s�mbolo j� est� no TextField
							if (!getText().contains(String.valueOf(e.getKeyChar()))) {
								
								//Verificando se o s�mbolo � o sinal de menos
								if (e.getKeyChar() == KeyEvent.VK_MINUS) {
									
									//Verificando o TextField est� vazio
									if (getText().length() == 0) {
										
										setText(getText() + e.getKeyChar());
									}
								} else {
									
									setText(getText() + e.getKeyChar());
								}
							}
						}
					} else {
						setText(getText() + e.getKeyChar());
					}
					
					//Verificando se a caixa de di�logo deve apenas receber valores do tipo inteiro ou real
					switch (tipo.toUpperCase()) {
						
						case "INT": {
							
							//Verificando se � poss�vel converter o valor no TextField para integer
							try {
								int n = Integer.parseInt(getText());
								
								//Verifica se o n�mero est� abaixo do m�nimo aceito
								if (n < min) {
									
									//Troca o conte�do para o m�nimo aceito
									setText(String.valueOf((int)min));
								}
								
								//Verifica se o n�mero est� acima do m�ximo aceito
								if (n > max) {
									
									//Troca o conte�do para o m�ximo aceito
									setText(String.valueOf((int)max));
								}
							} catch(Exception ex) {
								
								//Verifica se o caractere � -
								if (e.getKeyChar() != KeyEvent.VK_MINUS) {
									
									//Caso n�o seja poss�vel converter o n�mero o campo � preenchido com 0
									setText("0");
								}						
							}
							break;
						}
						case "DOUBLE": {
							
							//Verificando se � poss�vel converter o valor no TextField para double
							try {
								double n = Double.parseDouble(getText());
								
								//Verifica se o n�mero est� abaixo do m�nimo aceito
								if (n < min) {
									
									//Troca o conte�do para o m�nimo aceito
									setText(String.valueOf(min));
								}
								
								//Verifica se o n�mero est� acima do m�ximo aceito
								if (n > max) {
									
									//Troca o conte�do para o m�ximo aceito
									setText(String.valueOf(max));
								}
							} catch(Exception ex) {
								
								//Verifica se o caractere � -
								if (e.getKeyChar() != KeyEvent.VK_MINUS) {
									
									//Caso n�o seja poss�vel converter o n�mero o campo � preenchido com 0
									setText("0");
								}
							}
							
							break;
						}
					}
				}
				
				//Verifica se deve desenhar o placeholder
				if (getText().isEmpty()) {
					
					desenharPlaceholder();
				}
			}
			
			case KeyEvent.KEY_PRESSED: {
				
				//Obtendo lista de key listeners
				KeyListener[] listeners = getKeyListeners();
				
				//Passando evento para os key listeners
				for (KeyListener listener : listeners) {
					listener.keyPressed(e);
				}
			}
			
			case KeyEvent.KEY_RELEASED: {
				
				//Obtendo lista de key listeners
				KeyListener[] listeners = getKeyListeners();
				
				//Passando evento para os key listeners
				for (KeyListener listener : listeners) {
					listener.keyReleased(e);
				}
			}
		}
	}
	
	public void desenharPlaceholder() {
		
		//Verifica se o placeholder existe
		if (this.placeholder == null) {
			return;
		}
		
		//Obtendo componente para desenhar na tela
		Graphics g = getGraphics();
		
		//Definindo fonte do graphics
		g.setFont(new Font("Arial", Font.ITALIC, 12));
		
		//Obtendo largura do edit
		int editWidth = (int) getBounds().getWidth();
		
		//Obtendo largura do texto
		FontMetrics metrics = g.getFontMetrics();
		int widthTexto = metrics.stringWidth(placeholder);
		
		//Calculando coordenadas para o desenho do placeholder
		int x = (editWidth / 2) - (widthTexto / 2), y = 18;
		
		//Desenha o placeholder
		g.setColor(Color.GRAY);
		g.drawString(placeholder, x, y);
	}
}