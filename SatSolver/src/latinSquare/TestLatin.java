package latinSquare;

public class TestLatin {

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		int size = 2;
		for(int i = 0;i<8;i++) {
			int[] indices = LatinSquare.intToIndices(i, size);
			System.out.println(LatinSquare.indicesToString(indices));
		}
		System.out.println("test 2");
		for(int i = 0;i<size;i++) {
			for(int j= 0;j<size;j++) {
				for(int k = 0; k < size; k++) {
					System.out.println(LatinSquare.indicesToInt(i, j, k, size));
				}
			}
		}
	}

}
