package sort;

import java.util.Arrays;

public class C1_归并排序 {

    public static void main(String[] args) {
        int[] array = new int[]{10,9,8,7,6,5,4,3,2,1,15,19,2,14,100,11,50,32,31};
        System.out.println("排序前: " + Arrays.toString(array));
        sort(array,0, array.length-1);
        System.out.println("排序后: " + Arrays.toString(array));
    }



    public static void sort(int[] a, int low, int high) {
        if (low < high) {
            int mid = (low + high) / 2;
            // 左边归并排序
            sort(a, low, mid);
            // 右边归并排序
            sort(a, mid + 1, high);
            // 合并两个有序数组
            merge(a, low, mid, high);
        }
    }

    public static void merge(int[] a, int low, int mid, int high) {
        // 临时数组
        int[] temp = new int[high - low + 1];

        // 左右两边数组，挨个比较，填充进temp
        int i=low, j=mid+1, k = 0;
        while (i <= mid && j <= high) {
            if (a[i] < a[j])
                temp[k++] = a[i++];
            else
                temp[k++] = a[j++];
        }
        // 右边剩余元素填充进temp中（因为前面已经归并，剩余的元素必会小于右边剩余的元素）
        while (i <= mid) { temp[k++] = a[i++]; }
        // 右边剩余元素填充进temp中（因为前面已经归并，剩余的元素必会大于于左边剩余的元素）
        while (j <= high) { temp[k++] = a[j++];}

        // 用temp内容，覆盖原数组的这一段
        for (int x = 0; x < temp.length; x++) {
            a[x + low] = temp[x];
        }
    }
}
