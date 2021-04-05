package model.entities;

import java.io.Serializable;

public class Department implements Serializable{
	//implements "Serializable", nosso objetos podendo ser transformados em sequencia de bytes
	//Precisa ser implementado quand, ela e guardado em arquivos trafegado em redes
	private static final long serialVersionUID = 1L;
	
	private Integer id;
	private String name;
	
	public Department() {//construtor padrao
	}

	public Department(Integer id, String name) {
		this.id = id;
		this.name = name;
	}

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}
	//hashCode and equals 
	//Para que meus objetos, possam ser comparado pelo conteudo e nao pela referencia de ponteiro
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((id == null) ? 0 : id.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Department other = (Department) obj;
		if (id == null) {
			if (other.id != null)
				return false;
		} else if (!id.equals(other.id))
			return false;
		return true;
	}
	//Facilidade em imprimir valores, na hora do teste
	@Override
	public String toString() {
		return "Department [id=" + id + ", name=" + name + "]";
	}
	
}
