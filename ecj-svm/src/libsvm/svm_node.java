package libsvm;

import libsvm.svm_node;

public class svm_node implements java.io.Serializable, java.lang.Comparable<svm_node>
{
	public int index;
	public double value;

	public svm_node(){

	}
	
	public svm_node(int index, double value){
		this.index = index;
		this.value = value;
	}

	@Override
	public int compareTo(svm_node o) {
		if(this.index == o.index && this.value == o.value)
			return 0;
		else{
			if(this.value > o.value)
				return 1;
			else if(this.value < o.value)
				return -1;
			else{
				if(this.index > o.index)
					return 1;
				else
					return -1;
			}
				
		}
	}
	
	public boolean equals(Object o){
		if (o.getClass() != this.getClass())
			return false;			
		if (((svm_node)o).index == this.index && ((svm_node)o).value == this.value)
			return true;
		return false;
	}
	
}
