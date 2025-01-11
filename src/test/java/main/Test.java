package main;

import java.util.ArrayList;
import java.util.List;

public class Test {

	public static void main(String[] args) {
		int n = 16 * 16;
		int prev = 0;
		for (int i = 1; i < n; i++) {
			int count = 0;
			List<String> disp = new ArrayList<>();
			int rx = (int) Math.sqrt(i);
			for (int x = 0; x <= rx; x++) {
				int ry = (int) Math.sqrt(i - x * x);
				for (int y = 0; y <= ry; y++) {
					int z = (int) Math.sqrt(i - x * x - y * y);
					if (x == 0 && z == 0) continue;
					if (x * x + y * y + z * z == i) {
						int ans = 1;
						if (x != 0) ans *= 2;
						if (z != 0) ans *= 2;
						count += ans;
						if (z <= x)
							disp.add("(" + x + "," + y + "," + z + ")");
					}
				}
			}
			if (count > prev) {
				prev = count;
				System.out.println("RadSqr = " + i + ", count = " + count);
				boolean first = true;
				for (var e : disp) {
					if (first) first = false;
					else System.out.print(", ");
					System.out.print(e);
				}
				System.out.println();
			}
		}
	}

}
