package pozzo.apps.tools.sample;

import android.app.Activity;
import android.os.Bundle;
import android.widget.TextView;

import pozzo.apps.tools.cmd.Command;

public class OutputActivity extends Activity {
	public static final String PARAM_COMMAND = "command";

	private Command command;

	private TextView tvOutput;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_output);

		bindViews();
		readParams();
		populateScreen();
	}

	private void bindViews() {
		tvOutput = (TextView) findViewById(R.id.tvOutput);
	}

	private void readParams() {
		command = getIntent().getParcelableExtra(PARAM_COMMAND);
	}

	private void populateScreen() {
		tvOutput.setText(command.runToString());
	}
}
