package pozzo.apps.tools.cmd;

import android.os.Parcel;
import android.os.Parcelable;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

/**
 * Created by barrador on 06/11/16.
 */
public class Command implements Parcelable {
	private static final String COMMAND_SEPARATOR = " ";

	private String command;
	private Command subCommand;

	public Command(String command) {
		this.command = command;
	}

	public Command(String command, Command subCommand) {
		this.command = command;
		this.subCommand = subCommand;
	}

	protected Command(Parcel in) {
		command = in.readString();
		subCommand = in.readParcelable(Command.class.getClassLoader());
	}

	@Override
	public void writeToParcel(Parcel dest, int flags) {
		dest.writeString(command);
		dest.writeParcelable(subCommand, flags);
	}

	@Override
	public int describeContents() {
		return 0;
	}

	public static final Creator<Command> CREATOR = new Creator<Command>() {
		@Override
		public Command createFromParcel(Parcel in) {
			return new Command(in);
		}

		@Override
		public Command[] newArray(int size) {
			return new Command[size];
		}
	};

	public void setSubCommand(Command subCommand) {
		this.subCommand = subCommand;
	}

	public Command getSubCommand() {
		return subCommand;
	}

	/**
	 * Adds the command in the end of the command.
	 *
	 * @return this;
	 */
	public Command addSubCommand(Command command) {
		Command parent = this;
		Command child = subCommand;
		while (child != null) {
			parent = child;
			child = child.getSubCommand();
		}
		parent.setSubCommand(command);
		return this;
	}

	public Command addSubCommand(String command) {
		return addSubCommand(new Command(command));
	}

	public void runIgnoreAll() {
		try {
			Runtime.getRuntime().exec(toString());
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public InputStream runToInputStream() throws IOException {
		Process process = Runtime.getRuntime().exec(toString());
		return process.getInputStream();
	}

	public String runToString() {
		StringBuilder outputStr = new StringBuilder();
		try {
			InputStream output = runToInputStream();
			BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(output));

			String line;
			while ((line = bufferedReader.readLine()) != null) {
				outputStr.append(line).append("\n");
			}
		} catch (IOException e) {
			outputStr.append("\n\n\n\n").append(e.getMessage());
		}
		return outputStr.toString();
	}

	@Override
	public String toString() {
		String commandStr = command;
		Command child = subCommand;
		while (child != null) {
			commandStr += COMMAND_SEPARATOR + child.command;
			child = child.getSubCommand();
		}

		return commandStr;
	}
}
