package mtr.model;

public class ModelSTrainMini extends ModelSTrain {

	@Override
	protected int[] getWindowPositions() {
		return new int[]{0};
	}

	@Override
	protected int[] getDoorPositions() {
		return new int[]{-40, 40};
	}

	@Override
	protected int[] getEndPositions() {
		return new int[]{-64, 64};
	}

	@Override
	protected int[] getBogiePositions() {
		return new int[]{0};
	}
}