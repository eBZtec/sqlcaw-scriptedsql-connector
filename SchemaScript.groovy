import org.forgerock.openicf.connectors.scriptedsql.ScriptedSQLConfiguration
import org.forgerock.openicf.misc.scriptedcommon.ICFObjectBuilder
import org.forgerock.openicf.misc.scriptedcommon.OperationType
import org.identityconnectors.common.logging.Log
import org.identityconnectors.framework.common.objects.ObjectClass
import org.identityconnectors.framework.common.objects.OperationalAttributes
import org.identityconnectors.framework.spi.operations.SearchOp
import org.identityconnectors.framework.common.objects.OperationOptionInfoBuilder

def log = log as Log
def operation = operation as OperationType
def builder = builder as ICFObjectBuilder
def configuration = configuration as ScriptedSQLConfiguration

log.info("Entering " + operation + " Script")

builder.schema {
    objectClass {
        type ObjectClass.ACCOUNT_NAME
        attributes {
            cod_usu()
            cod_grupo()
            cod_parceiro()
            cod_tipo_usu()
            dat_alteraca_senha()
            dat_ultimo_acesso()
            dsc_email()
            dsc_senha()
            flg_ativo()
            flg_usu_bloqueado()
            id_usu()
            idc_ctl_sen_exn()
            nom_usu()
            qtd_senha_invalida()
            OperationalAttributes.PASSWORD_NAME
        }
    }

    defineOperationOption OperationOptionInfoBuilder.buildPagedResultsOffset(), SearchOp
    defineOperationOption OperationOptionInfoBuilder.buildPageSize(), SearchOp
}