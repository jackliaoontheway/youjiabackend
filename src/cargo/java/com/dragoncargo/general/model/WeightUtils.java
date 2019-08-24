package com.dragoncargo.general.model;

public class WeightUtils {

    public static Double getVolume(Double length, Double width, Double height) {
		return length * width * height;
	}

	public static Double getVolumeWeight(Double length, Double width, Double height) {
		return length * width * height / 6000;
	}

	public static Double getActualWeight(Double grossWeight, Double volumeWeight) {
		return grossWeight > volumeWeight ? grossWeight : volumeWeight;
	}
}
