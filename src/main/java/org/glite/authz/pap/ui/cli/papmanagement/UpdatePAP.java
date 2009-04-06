package org.glite.authz.pap.ui.cli.papmanagement;

import java.rmi.RemoteException;

import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.OptionBuilder;
import org.apache.commons.cli.Options;
import org.apache.commons.cli.ParseException;
import org.glite.authz.pap.common.Pap;

public class UpdatePAP extends PAPManagementCLI {

    private static final String[] commandNameValues = { "update-pap", "upap" };
    private static final String DESCRIPTION = "Update pap information. The input is the same as for the \"add-pap\" command, " +
    		"the effect is to update old information with the new one.\n";
    private static final String LOPT_PRIVATE = "private";
    private static final String LOPT_PUBLIC = "public";
    private static final String USAGE = "[options] <alias> [<endpoint> <dn>]";

    public UpdatePAP() {
        super(commandNameValues, USAGE, DESCRIPTION, null);
    }

    @SuppressWarnings("static-access")
    @Override
    protected Options defineCommandOptions() {
        Options options = new Options();
        options.addOption(OptionBuilder.hasArg(false)
                                       .withDescription("Set the remote PAP as public (allow to distribute its policies)")
                                       .withLongOpt(LOPT_PUBLIC)
                                       .create());
        options.addOption(OptionBuilder.hasArg(false)
                                       .withDescription("Set the remote PAP as private (default)")
                                       .withLongOpt(LOPT_PRIVATE)
                                       .create());
        options.addOption(OptionBuilder.hasArg(false)
                                       .withDescription(OPT_LOCAL_DESCRIPTION)
                                       .withLongOpt(OPT_LOCAL_LONG)
                                       .create(OPT_LOCAL));
        options.addOption(OptionBuilder.hasArg(false)
                                       .withDescription(OPT_REMOTE_DESCRIPTION)
                                       .withLongOpt(OPT_REMOTEL_LONG)
                                       .create(OPT_REMOTE));
        return options;
    }

    @Override
    protected int executeCommand(CommandLine commandLine) throws ParseException, RemoteException {
        String[] args = commandLine.getArgs();

        if ((args.length != 4) && (args.length != 2)) {
            throw new ParseException("Wrong number of arguments");
        }

        boolean isLocal;
        boolean isPublic = false;

        if (args.length != 2) {
            isLocal = false;
        } else {
            isLocal = true;
        }

        if (commandLine.hasOption(OPT_LOCAL)) {
            isLocal = true;
        }

        if (commandLine.hasOption(OPT_REMOTE)) {
            isLocal = true;
        }

        if (commandLine.hasOption(LOPT_PUBLIC)) {
            isPublic = true;
        }

        String alias = args[1];
        String protocol = null;
        String dn = null;
        String host = null;
        String port = null;
        String path = null;

        if (args.length != 2) {
            protocol = AddPAP.getProtocol(args[2]);
            host = AddPAP.getHostname(args[2]);
            port = AddPAP.getPort(args[2]);
            path = AddPAP.getPath(args[2]);
        }
        
        Pap pap = new Pap(alias, isLocal, dn, host, port, path, protocol, isPublic);

        String msg = "Updating PAP: ";

        if (verboseMode) {
            System.out.println(msg + pap.toFormattedString(0, msg.length()));
        }

        if (!(papMgmtClient.exists(pap.getAlias()))) {
            System.out.println("PAP doesn't exists.");
            return ExitStatus.FAILURE.ordinal();
        }

        papMgmtClient.updatePAP(pap);

        if (verboseMode) {
            System.out.println("Success: pap has been updated.");
        }

        return ExitStatus.SUCCESS.ordinal();
    }
}
