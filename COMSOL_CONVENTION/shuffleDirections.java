void shuffleDirections(int[][] a, java.util.Random random) {
    for (int i = a.length - 1; i > 0; i--) {
        int j = random.nextInt(i + 1);
        int[] temp = a[i];
        a[i] = a[j];
        a[j] = temp;
    }
}