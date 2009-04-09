package org.glite.authz.pap.ui.cli.papmanagement;

import java.rmi.RemoteException;

import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.Options;
import org.apache.commons.cli.ParseException;

public class RemovePap extends PAPManagementCLI {

    private static final String[] commandNameValues = { "remove-pap", "rpap" };
    private static final String DESCRIPTION = "Remove a pap and delete its policies.";
    private static final String USAGE = "<alias>";

    public RemovePap() {
        super(commandNameValues, USAGE, DESCRIPTION, null);
    }

    @Override
    protected Options defineCommandOptions() {
        return null;
    }

    @Override
    protected int executeCommand(CommandLine commandLine) throws ParseException, RemoteException {
        String[] args = commandLine.getArgs();

        if (args.length != 2) {
            throw new ParseException("Wrong number of arguments");
        }

        String papId = args[1];

        if (!papMgmtClient.exists(papId)) {
            System.out.println("PAP not found: " + papId);
            return ExitStatus.FAILURE.ordinal();
        }

        papMgmtClient.removePap(papId);

        return ExitStatus.SUCCESS.ordinal();

    }

}