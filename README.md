# SmartX Micro(μ)-Box 
OF@TEIN+ playground launched to serve as SDN-enabled multi-site clouds for miniaturized academic experiments is a miniaturized, software-defined playground with hyper-converged box-style resources. It is an overlay-interconnected, multi-site playground over heterogeneous (3 ∼7 inter-connected) underlay networks.

![alt text](https://github.com/SmartX-Team/SmartX-MicroBox/blob/master/basic_functionality/Fig2.png)

In the given environment, we placed cloud-native edge boxes termed **“SmartX Micro-Box”** aka Micro-Box, distributed at multi-site edge locations of OF@TEIN+ playground. These Micro-Boxes are commodity server-based hyper-converged resources (compute /storage/networking) to allow experiments (Cloud/Software-defined networking/Network Function
Virtualization) overOF@TEIN+playground.

Supported by cloud-native (containerized) functionalities,Micro-Box is prepared as Kubernetes-orchestrated workers with SDN-coordinated special connectivity to other Micro-Boxes. The tenant awareness of deployed containerized application provides isolation.

In terms of visibility measurements among multiple distributed sites, Micro-Boxes are configured in mesh-style networking,where visibility collection is reported to the centralized Visibility Center at the SmartX Playground Tower. As depicted in Figure , the applications running in the Micro-Box are termed as **“Functionalities,”** whereas the corresponding applications at the Tower Centers are termed as **“Companion functionalities.”** The software version in the Micro-Box is named as “$ (Dollar)” and the “Core functionalities” consist of software functionality that manages the Micro-Box operations such as OS, kernel, Kubernetes, etc. “Basic functionalities” are additional development at the bare
metal and constitutes monitoring of connection status/health with connected Micro-Boxes in the playground, ensuring collection consistency and service reliability. The **“Application functionalities”** consist of applications in the form factor of containers orchestrated through Kubernetes. Maintaining persistent visibility is crucial for the effective operation of OF@TEIN+ playground to ensure reliable operations with timely knowledge of issues concerning playground.

Furthermore, to ensure reliable and uninterrupted operation, we collected regular connection status of Micro-Box
with the Visibility Center called “liveliness status of Micro-Box,” as well as connection status with other Micro-Boxes
called “interconnection liveliness.” Reporting is action taken byMicro-box to an assigned task (eg, sending collected measurements
to the center, whereas response is the action taken at Visibility Center to an event generated fromMicro-box).
Measurements are transferred through a shared messaging interface ensuring reliable communication and resistance
against losses. To ensure persistent execution of functionalities, Agents monitors and starts stopped services. Box-Agent
asynchronously communicates with Center-Agent with status messages. Persistent storage stores data for real-time and
temporal analysis.
