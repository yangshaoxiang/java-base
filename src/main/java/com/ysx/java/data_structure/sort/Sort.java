package com.ysx.java.data_structure.sort;

import java.util.Arrays;
import java.util.HashMap;

public class Sort {

    public static void main1(String[] args) {
       int[] nums = {4,6,8,1,2,9,0,3};
        //int[] nums = {6,4};
        int[] sortnums = {0, 1, 2, 3, 4, 6, 8, 9};
        bubblingSort(nums);
        System.out.println(Arrays.toString(nums));
       // insertSort(nums);
       // choseSort(nums);
   /*     int[] mergeSort = mergeSort(nums);
        System.out.println(Arrays.toString(mergeSort));*/
    }


    /**
     * 冒泡排序 - 从小到大
     * 思想:从头开始比较相邻元素大小，做交换，一次循环可以确定一个最大元素
     * @param nums 要排序的数组
     */
    public  static void bubblingSort(int[] nums){
        int length = nums.length;
        int count = 0;//外层循环次数
        for(int i=0;i<length;i++){
            boolean exitFlag = true;
            count++;
            for (int j=i+1;j<length;j++){
                int current = nums[i];
                int next = nums[j];
                if(current>next){
                    exitFlag = false;
                    nums[i] = next;
                    nums[j] = current;
                }
            }
            if(exitFlag){
                break;
            }
        }
        System.out.println("外层循环:"+count+"次");
    }

    /**
     * 插入排序- 从小到大
     * 思想:将数组看为两部分，一部分排好序，另一部分未排序，将未排序部分插入到已排序部分
     * @param nums 要排序的数组
     *             ps：代码中的current ，index都可省略节省空间，方便理解，这样写
     */
    public static void insertSort(int[] nums){
        int length = nums.length;
        for(int i=1;i<length;i++){
            int current = nums[i];
            //遍历有序数组，找到要插入的位置,没找到说明位置就是当前所处位置
            int index = i;
            //是否已找到要插入的位置  找到的话接下来做数据搬迁
           // boolean findIndex = false;
            for (int j=i-1;j>=0;j--){
                //在插入的位置后面，后面数据要向后搬迁一格，被搬移的位置相当于初步找到的要插入位置
                if(nums[j]>current){
                    nums[j+1] = nums[j];
                    index = j;
                }else{
                    break;
                }
            }
            nums [index] =  current;
        }
    }


    /**
     * 选择排序-从小到大
     * 思想:每次找出数组的最小元素，放在当前循环的起始位置
     * @param nums 要排序的数组
     */
    public  static void choseSort(int[] nums){
        for(int i=0;i<nums.length;i++){
            for (int j=i+1;j<nums.length;j++){
                if(nums[i]>nums[j]){
                    int tem = nums[i];
                    nums[i] = nums[j];
                    nums[j] = tem;
                }
            }
        }
    }

    //----------------------------归并排序---------------------------
    /**
     * 归并排序- 从小到大
     * 思想:分治的思想，将数组一直一份为二，然后将排好序的小数组合并为最终的大数组
     * @param nums 要排序的数组
     */
    public static int[] mergeSort(int[] nums){
        if(nums.length<2) return nums;
        int mid = nums.length/2;
        //拷贝数组 参数: 原始数组 拷贝的起点位置Index 拷贝的终点位置Index(不包含)
        int[] left = Arrays.copyOfRange(nums,0,mid);
        int[] right = Arrays.copyOfRange(nums,mid,nums.length);
        //合并排好序的数组
        return mergeNums(mergeSort(left),mergeSort(right));
    }
    /**
     * 归并排序合并两个已排序数组
     * @param mergeLeft 左数组
     * @param mergeRight 右数组
     * @return 合并后且已排序的数组
     */
    private static int[] mergeNums(int[] mergeLeft, int[] mergeRight) {
        int[] tmp = new int[mergeLeft.length+mergeRight.length];
        int leftIdx = 0;
        int rightIdx = 0;
        for (int i=0;i<tmp.length;i++ ){
            //左数组越界表明左数组已经全部添加到临时数组中
            if(leftIdx>mergeLeft.length-1){
                //可写为 tmp[i] = mergeRight[rightIdx++];  简化代码 这里方便理解不这样写了
                tmp[i] = mergeRight[rightIdx];
                rightIdx++;
                break;
            }
            //同理
            if(rightIdx>mergeRight.length-1){
                //可写为 tmp[i] = mergeLeft[leftIdx++];简化代码
                tmp[i] = mergeLeft[leftIdx];
                leftIdx++;
                break;
            }
            //比较左右数组中值，值小的放入到临时数组中，并将索引指向对应数组下一元素
            int mergeLeftNum = mergeLeft[leftIdx];
            int mergeRightNum = mergeRight[rightIdx];
            if(mergeLeftNum<=mergeRightNum){
                tmp[i] = mergeLeftNum;
                leftIdx++;
            }else{
                tmp[i] = mergeRightNum;
                rightIdx++;
            }
        }
        return tmp;
    }



}
