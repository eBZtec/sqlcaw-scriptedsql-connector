import groovy.sql.Sql
import groovy.text.SimpleTemplateEngine
import org.forgerock.openicf.connectors.scriptedsql.ScriptedSQLConfiguration
import org.forgerock.openicf.misc.scriptedcommon.OperationType
import org.identityconnectors.common.logging.Log
import org.identityconnectors.framework.common.objects.ObjectClass
import org.identityconnectors.framework.common.objects.OperationOptions
import org.identityconnectors.framework.common.objects.ResultsHandler
import org.identityconnectors.framework.common.objects.filter.Filter

import javax.naming.directory.SearchResult
import java.sql.Connection

def log = log as Log
def operation = operation as OperationType
def options = options as OperationOptions
def objectClass = objectClass as ObjectClass
def configuration = configuration as ScriptedSQLConfiguration
def filter = filter as Filter
def connection = connection as Connection
def query = query as Closure
def handler = handler as ResultsHandler

def sql = new Sql(connection)
def result = []
def where = "";

if (query != null){
    //Need to handle the __UID__ in queries
    if (query.get("left").equalsIgnoreCase("__UID__") && objectClass.equalsIgnoreCase("__ACCOUNT__")) query.put("left","id");
    if (query.get("left").equalsIgnoreCase("__UID__") && objectClass.equalsIgnoreCase("Group")) query.put("left","id");
    if (query.get("left").equalsIgnoreCase("__UID__") && objectClass.equalsIgnoreCase("Organization")) query.put("left","id")

    // We can use Groovy template engine to generate our custom SQL queries
    def engine = new SimpleTemplateEngine();

    def whereTemplates = [
            CONTAINS:' WHERE $left ${not ? "NOT " : ""}LIKE "%$right%"',
            ENDSWITH:' WHERE $left ${not ? "NOT " : ""}LIKE "%$right"',
            STARTSWITH:' WHERE $left ${not ? "NOT " : ""}LIKE "$right%"',
            EQUALS:' WHERE $left ${not ? "<>" : "="} \'$right\'',
            GREATERTHAN:' WHERE $left ${not ? "<=" : ">"} "$right"',
            GREATERTHANOREQUAL:' WHERE $left ${not ? "<" : ">="} "$right"',
            LESSTHAN:' WHERE $left ${not ? ">=" : "<"} "$right"',
            LESSTHANOREQUAL:' WHERE $left ${not ? ">" : "<="} "$right"'
    ]

    def wt = whereTemplates.get(query.get("operation"));
    def binding = [left:query.get("left"),right:query.get("right"),not:query.get("not")];
    def template = engine.createTemplate(wt).make(binding);
    where = template.toString();
    log.ok("Search WHERE clause is: "+ where)
}

switch ( objectClass ) {
    case ObjectClass.ACCOUNT_NAME:
        sql.eachRow("SELECT * FROM usuario" + where, {
            result.add([
                    __UID__: it.cod_usu,
                    __NAME__: it.cod_usu,
                    __ENABLE__: true,
                    cod_grupo: it.cod_grupo,
                    cod_parceiro: it.cod_parceiro,
                    cod_tipo_usu: it.cod_tipo_usu,
                    cod_usu: it.cod_usu,
                    dat_alteracao_senha: it.dat_alteracao_senha,
                    dat_ultimo_acesso: it.dat_ultim_acesso,
                    dsc_email: it.dsc_email,
                    flg_ativo: it.flg_ativo,
                    flg_usu_bloqueado: it_flg_usu_bloqueado,
                    id_usu: it.id_usu,
                    idc_ctl_sen_exn: it.idc_ctl_sen_exn,
                    nom_usu: it.nom_usu,
                    qtd_senha_invalida: it.qtd_senha_invalida
            ])} );
        break

    default:
        result;
}

return result;