import org.forgerock.openicf.connectors.scriptedsql.ScriptedSQLConfiguration
import org.forgerock.openicf.misc.scriptedcommon.ICFObjectBuilder
import org.forgerock.openicf.misc.scriptedcommon.OperationType
import org.identityconnectors.common.logging.Log
import org.identityconnectors.framework.common.objects.ObjectClass
import org.identityconnectors.framework.common.objects.OperationalAttributes
import org.identityconnectors.framework.spi.operations.SearchOp
import org.identityconnectors.framework.common.objects.OperationOptionInfoBuilder

import java.time.ZonedDateTime

import static org.identityconnectors.framework.common.objects.AttributeInfo.Flags.REQUIRED

def log = log as Log
def operation = operation as OperationType
def builder = builder as ICFObjectBuilder
def configuration = configuration as ScriptedSQLConfiguration

log.info("Entering " + operation + " Script")

builder.schema {
    objectClass {
        type ObjectClass.ACCOUNT_NAME
        attribute OperationalAttributes.PASSWORD_NAME
        attribute OperationalAttributes.ENABLE_NAME
        attributes {
            cod_grupo String.class
            cod_parceiro String.class
            cod_tipo_usu String.class
            cod_usu String.class, REQUIRED
            dat_alteracao_senha ZonedDateTime.class
            dat_ultimo_acesso ZonedDateTime.class
            dsc_email String.class
            flg_ativo String.class
            flg_usu_bloqueado String.class
            id_usu String.class
            idc_ctl_sen_exn String.class
            nom_usu String.class
            qtd_senha_invalida String.class
        }
    }

    defineOperationOption OperationOptionInfoBuilder.buildPagedResultsCookie(), SearchOp
    defineOperationOption OperationOptionInfoBuilder.buildPagedResultsOffset(), SearchOp
    defineOperationOption OperationOptionInfoBuilder.buildPageSize(), SearchOp
    defineOperationOption OperationOptionInfoBuilder.buildSortKeys(), SearchOp
    defineOperationOption OperationOptionInfoBuilder.buildRunWithUser()
    defineOperationOption OperationOptionInfoBuilder.buildRunWithPassword()
}