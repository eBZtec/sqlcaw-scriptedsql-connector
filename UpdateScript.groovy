import groovy.sql.Sql
import org.forgerock.openicf.connectors.scriptedsql.ScriptedSQLConfiguration
import org.forgerock.openicf.misc.scriptedcommon.OperationType
import org.identityconnectors.common.logging.Log
import org.identityconnectors.framework.common.objects.Attribute
import org.identityconnectors.framework.common.objects.Name
import org.identityconnectors.framework.common.objects.ObjectClass
import org.identityconnectors.framework.common.objects.OperationOptions
import org.identityconnectors.framework.common.objects.Uid

import java.sql.Connection

def log = log as Log
def operation = operation as OperationType
def options = options as OperationOptions
def objctClass = objectClass as ObjectClass
def attributes = attributes as Set<Attribute>
def connection = connection as Connection
def uid = uid as Uid
def id = id as Name
def configuration = configuration as ScriptedSQLConfiguration

log.info("Entering " + operation + " Script")

def sql = new Sql(connection)