package com.inge.edecisiones;

import java.util.ArrayList;
import java.util.List;

public interface LoginStatusChangeNotifier {
	public void notificar();
	public void addListener(LoginStatusChangeListener listener);
	public void removeListener(LoginStatusChangeListener listener);
}
