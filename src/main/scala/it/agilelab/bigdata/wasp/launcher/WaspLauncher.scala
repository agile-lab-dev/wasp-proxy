package it.agilelab.bigdata.wasp.launcher

import it.agilelab.bigdata.wasp.core.WaspSystem
import it.agilelab.bigdata.wasp.core.utils.ActorSystemInjector

trait WaspLauncher extends ActorSystemInjector  {

	val producersAkkaTopic = "producers"
	// ASCII art from http://bigtext.org/?font=smslant&text=Wasp
	val banner = """Welcome to
     _       __
    | |     / /___ _ ____ ___
    | | /| / / __ `/ ___/ __ \
    | |/ |/ / /_/ (__  ) /_/ /
    |__/|__/\__,_/____/ .___/    version %s
                     /_/
								               """.format(it.agilelab.bigdata.wasp.WASP_VERSION)

	// TODO write usage information (when command line switches are somewhat definitive)
	val usage = """Usage:
			TODO!
		          """.stripMargin

	def main(args: Array[String]) {
		WaspSystem.initializeActorSystem(actorSystemName)

		println(banner)
		initializeWasp(args)
	}

	def initializeWasp(args: Array[String]) = {
		// workloads
		initializeCustomWorkloads(args)
	}





		/**
		* Launchers must override this with deployment-specific pipegraph initialization logic;
		* this usually simply means loading the custom pipegraphs into the database.
		*/
	def initializeCustomWorkloads(args: Array[String]): Unit
}