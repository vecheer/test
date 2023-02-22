package sort;

import java.util.Arrays;

public class C2_快速排序 {
    public static void main(String[] args) {
        int[] array = new int[]{10,9,8,7,6,5,4,3,2,1,15,19,2,14,100,11,50,32,31};
        System.out.println("快速排序前:"+ Arrays.toString(array));
        quickSort(array,0,array.length-1);
        System.out.println("快速排序后:"+ Arrays.toString(array));
    }
    public static void quickSort(int[] nums, int start, int end){
        if(start>=end) return;
        int i,j,base;
        i=start;
        j=end;
        base=nums[start];
        while (i<j){
            while (i<j && nums[j]>=base) j--;
            while (i<j && nums[i]<=base) i++;
            if(i<j){
                swap(nums,i,j);
            }
        }
        swap(nums,start,i);
        quickSort(nums,start,j-1);
        quickSort(nums,j+1,end);
    }
    public static void swap(int[] nums,int left,int right){
        int temp=nums[left];
        nums[left]=nums[right];
        nums[right]=temp;
    }
}
