/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.alecn.glo.action;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import org.netbeans.api.io.IOProvider;
import org.openide.awt.ActionID;
import org.openide.awt.ActionReference;
import org.openide.awt.ActionRegistration;
import org.openide.util.NbBundle.Messages;
import org.netbeans.api.io.InputOutput;

@ActionID(
        category = "Team",
        id = "com.alecn.glo.action.GetBoardListAction"
)
@ActionRegistration(
        displayName = "#CTL_GetBoardListAction"
)
@ActionReference(path = "Menu/Versioning", position = 0, separatorAfter = 50)
@Messages("CTL_GetBoardListAction=get Glo Boards list")
public final class GetBoardListAction implements ActionListener {

    @Override
    public void actionPerformed(ActionEvent e) {
        InputOutput io = IOProvider.getDefault().getIO ("Hello", true);
        io.getOut().println ("Hello from standard out");
        io.getErr().println ("Hello from standard err");  //this text should appear in red
        io.getOut().close();
        io.getErr().close();
    }
}
