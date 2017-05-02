package pozzo.apps.tools.sample;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import pozzo.apps.tools.cmd.Command;
import pozzo.apps.tools.cmd.Su;

public class MainActivity extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
	}

	private void displayOutput(Command command) {
		Intent intent = new Intent(this, OutputActivity.class);
		intent.putExtra(OutputActivity.PARAM_COMMAND, command);
		startActivity(intent);
	}

	public void reboot(View view) {
		new Su().addSubCommand("reboot").runIgnoreAll();
	}

	public void ifconfig(View view) {
		displayOutput(new Su().addSubCommand("ifconfig"));
	}
}
