package userData;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

public class DeepCopier {

	/*
	 * ���� ������ ��
	 * 
	 * ����ȭ�� �̿��ϴ� ����̹Ƿ� ����ȭ�� �������� �ʴ� Ŭ������ �����ҷ���
	 * ����ȭ ������ Ŭ������ ��ȯ�Ͽ����Ѵ�. ex) BufferedImage --> ImageIcon
	 * ���� ����ȭ �� transient�� ����� ������ �ٽ� ������� �������־�� �Ѵ�.
	 */
	public static Object deepCopy(Object original) throws Exception {
		ObjectOutputStream output = null;
		ObjectInputStream input = null;
		try {
			
			ByteArrayOutputStream byteOutput = new ByteArrayOutputStream();
			
			output = new ObjectOutputStream(byteOutput);
			output.writeObject(original);
			output.flush();
			
			ByteArrayInputStream byteInput = new ByteArrayInputStream(byteOutput.toByteArray());
			input = new ObjectInputStream(byteInput);
			
			return input.readObject();
			
		} catch (Exception e) {
			throw e;
		} finally {
			if(output != null)	output.close();
			if(input != null)	input.close();
		}
	}

}
