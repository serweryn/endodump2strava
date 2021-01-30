package endodump2strava.db

import io.getquill.{H2JdbcContext, SnakeCase}

class Queries(val sqlCtx: H2JdbcContext[SnakeCase]) {

  import sqlCtx._

  // token_info

  def selectTokenInfo(user: String): List[TokenInfo] =
    sqlCtx.run {
      quote {
        query[TokenInfo].filter(_.user == lift(user))
      }
    }

  def deleteTokenInfo(user: String): Long =
    sqlCtx.run {
      quote {
        query[TokenInfo].filter(_.user == lift(user)).delete
      }
    }

  def insertTokenInfo(tokenInfo: TokenInfo): Long =
    sqlCtx.run {
      quote {
        query[TokenInfo].insert(lift(tokenInfo))
      }
    }

  // imported_activity

  def selectActivity(workoutBaseName: String): List[ImportedActivity] =
    sqlCtx.run {
      quote {
        query[ImportedActivity].filter(_.workoutBasename == lift(workoutBaseName))
      }
    }

  def completedActivities(): List[ImportedActivity] =
    sqlCtx.run {
      quote {
        query[ImportedActivity].filter { a => a.activityId.nonEmpty &&
          query[ImportedActivityStep].filter(s => s.workoutBasename == a.workoutBasename &&
            s.stepName == lift(ImportedActivityStep.updateActivity) && s.responseCode < 300).nonEmpty
        }
      }
    }

  def insertActivity(activity: ImportedActivity): Long =
    sqlCtx.run {
      quote {
        query[ImportedActivity].insert(lift(activity))
      }
    }

  def deleteActivity(workoutBaseName: String): Long =
    sqlCtx.run {
      quote {
        query[ImportedActivity].filter(_.workoutBasename == lift(workoutBaseName)).delete
      }
    }

  def updateActivity(activity: ImportedActivity): Long =
    sqlCtx.run {
      quote {
        query[ImportedActivity].filter(_.workoutBasename == lift(activity.workoutBasename)).update(lift(activity))
      }
    }

  // imported_activity_step

  def selectActivityStep(basename: String, step: String): List[ImportedActivityStep] =
    sqlCtx.run {
      quote {
        query[ImportedActivityStep].filter(x => x.workoutBasename == lift(basename) && x.stepName == lift(step))
      }
    }

  def insertActivityStep(step: ImportedActivityStep): Long =
    sqlCtx.run {
      quote {
        query[ImportedActivityStep].insert(lift(step))
      }
    }

  def deleteActivityStep(step: ImportedActivityStep): Long = deleteActivityStep(step.workoutBasename, step.stepName)
  def deleteActivityStep(basename: String, step: String): Long =
    sqlCtx.run {
      quote {
        query[ImportedActivityStep].filter(x => x.workoutBasename == lift(basename) && x.stepName == lift(step)).delete
      }
    }

}
